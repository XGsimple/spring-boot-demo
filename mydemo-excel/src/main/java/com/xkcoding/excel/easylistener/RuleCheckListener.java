package com.xkcoding.bf.easylistener;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.RowTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import com.alibaba.excel.util.ListUtils;
import com.xkcoding.bf.dto.ExcelReadTableVO;
import com.xkcoding.bf.dto.ExcelSheetReadContext;
import com.xkcoding.bf.dto.ValidateFailRow;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class RuleCheckListener<T> extends AnalysisEventListener<T> {
    /**
     * 验证器对象，用来验证数据是否符合规范
     */
    private final static Validator validator;
    /**
     * 是否继续读取数据，默认为true，当读取到不符合规则的数据时，读取结束
     */
    private boolean isContinue = true;

    /**
     * 缓存的数据
     */
    private final List<T> cachedDataList = ListUtils.newArrayListWithExpectedSize(100);

    /**
     * 检查错误
     */
    private List<ValidateFailRow<T>> validateFailRows = new ArrayList<>();

    private Map<String, Integer> fieldColumnMapping;
    private ExcelSheetReadContext excelSheetReadContext;

    /**
     * 当前子表已读行数
     */
    private Integer readTableRows = 0;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public RuleCheckListener(ExcelSheetReadContext excelSheetReadContext) {
        this.excelSheetReadContext = excelSheetReadContext;
    }

    /**
     * 每读到一行数据都会调用这个方法进行消费,由子类去实现扩展
     *
     * @param data    数据行
     * @param context 上下文
     */
    @Override
    public void invoke(T data, AnalysisContext context) {
        if (context.readRowHolder().getRowType().equals(RowTypeEnum.EMPTY)) {
            stop();
            //需要手动吊用，否则不会触发doAfterAllAnalysed方法
            doAfterAllAnalysed(context);
            return;
        }
        try {
            //基于validate注解统一校验
            check(data, context).ifPresent(validateFailRows::add);
            //子表自定义行数据处理
            Optional.ofNullable(excelSheetReadContext.getCurrentReadTable().getRowDataReadHandler())
                    .ifPresent(theRowDataReadHandler -> {
                        try {
                            theRowDataReadHandler.handle(data);
                        } catch (Exception e) {
                            ExcelReadTableVO currentReadTable = excelSheetReadContext.getCurrentReadTable();
                            log.warn("子表自定义行数据处理出错，sheetName={},tableName={},子表对应模型Class={},子表间隔行数={},表头所占行数={},错误信息={}",
                                     excelSheetReadContext.getSheetName(),
                                     currentReadTable.getTableName(),
                                     currentReadTable.getClazz().getSimpleName(),
                                     currentReadTable.getRelativeHeadRowIndex(),
                                     currentReadTable.getHeadRows(),
                                     e.getMessage(),
                                     e);
                        }
                    });
            readTableRows++;
            cachedDataList.add(data);
        } catch (Exception e) {
            log.error(" 子表行数据自定义读处理出错，或数据校验失败,excelSheetReadContext={}",
                      JSONUtil.toJsonStr(excelSheetReadContext),
                      e);
        }
    }

    /**
     * 读取完所有数据会来调用,可以用来做一些善后工作
     *
     * @param context 上下文
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        try {
            ExcelReadTableVO<T> currentReadTable = excelSheetReadContext.getCurrentReadTable();
            currentReadTable.buildParsedResult(cachedDataList, validateFailRows);
            //子表数据自定义校验扩展
            Optional.ofNullable(currentReadTable.getTableDataReadHandler())
                    .ifPresent(theTableDataConsumer -> theTableDataConsumer.handle(cachedDataList, validateFailRows));
            excelSheetReadContext.updateRowIndex(cachedDataList.size());
            //更新sheet页是否有检验错误，如果有，数据不会保存到数据库
            if (!CollectionUtils.isEmpty(validateFailRows)) {
                excelSheetReadContext.setHasValidateError(Boolean.TRUE);
            }
        } catch (Exception e) {
            log.error("doAfterAllAnalysed报错,excelSheetReadContext={}", JSONUtil.toJsonStr(excelSheetReadContext), e);
        }
    }

    /**
     * 是否读取下一行, 如果返回false将不会继续读下去
     *
     * @param context 上下文
     * @return 是否继续读
     */
    @Override
    public final boolean hasNext(AnalysisContext context) {
        return isContinue;
    }

    /**
     * 停止读取数据行
     */
    public final void stop() {
        this.isContinue = false;
    }

    /**
     * 对数据行进行规则校验
     *
     * @param data    数据行
     * @param context 上下文
     * @return 校验结果集, 如果为空说明校验通过
     */
    public final Optional<ValidateFailRow<T>> check(T data, AnalysisContext context) {
        try {
            Set<ConstraintViolation<T>> validateResult = validator.validate(data);
            if (CollectionUtils.isEmpty(validateResult)) {
                return Optional.empty();
            }
            ReadRowHolder readRowHolder = context.readRowHolder();
            Integer rowIndex = readRowHolder.getRowIndex();
            ValidateFailRow<T> validateFailRow = new ValidateFailRow<T>(rowIndex, readTableRows);
            validateFailRow.setErrorData(data);
            List<ValidateFailRow.CheckFailColumn> checkFailColumns = validateResult.stream().map(violation -> {
                String field = violation.getPropertyPath().toString();
                Integer columnIndex = this.getColumnIndex(field, context);
                ValidateFailRow.CheckFailColumn checkFailColumn = new ValidateFailRow.CheckFailColumn();
                checkFailColumn.setColumnIndex(columnIndex);
                checkFailColumn.setMessage(violation.getMessage());
                checkFailColumn.setPropertyValue(violation.getInvalidValue());
                return checkFailColumn;
            }).collect(Collectors.toList());
            validateFailRow.setCheckFailColumns(checkFailColumns);
            return Optional.of(validateFailRow);
        } catch (Exception e) {
            log.error("excel数据校验失败", e);
            throw e;
        }
    }

    /**
     * 列属性从context获取列号
     *
     * @param field   属性名称
     * @param context excel上下文
     * @return 列号
     */
    private Integer getColumnIndex(final String field, final AnalysisContext context) {
        if (Objects.nonNull(this.fieldColumnMapping)) {
            return this.fieldColumnMapping.get(field);
        }
        synchronized (this) {
            if (Objects.nonNull(this.fieldColumnMapping)) {
                return this.fieldColumnMapping.get(field);
            }
            this.fieldColumnMapping = new HashMap<>(16);
            final Map<Integer, Head> excelFileHead = context.readSheetHolder().excelReadHeadProperty().getHeadMap();
            for (final Map.Entry<Integer, Head> entry : excelFileHead.entrySet()) {
                this.fieldColumnMapping.put(entry.getValue().getFieldName(), entry.getKey());
            }
            return this.fieldColumnMapping.get(field);
        }
    }

}