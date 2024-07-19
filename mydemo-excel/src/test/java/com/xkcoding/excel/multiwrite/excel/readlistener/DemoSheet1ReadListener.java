package com.xkcoding.excel.multiwrite.excel.readlistener;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.xkcoding.excel.multiwrite.excel.MultiSheetsWriteTest;
import com.xkcoding.excel.multiwrite.excel.dto.DemoSheet1Data;
import com.xkcoding.excel.multiwrite.excel.utils.ValidatorUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 模板的读取类
 */
// 有个很重要的点 DemoSheet1DataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
@Slf4j
@NoArgsConstructor
public class DemoSheet1ReadListener implements ReadListener<DemoSheet1Data> {

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    /**
     * 缓存的数据
     */
    private List<DemoSheet1Data> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    private Boolean bValidate = false;

    public DemoSheet1ReadListener(Boolean bValidate) {
        this.bValidate = bValidate;
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. It is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(DemoSheet1Data data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSONUtil.toJsonStr(data));
        if (bValidate) {
            Set<ConstraintViolation<Object>> validateSet = ValidatorUtil.validateAndReturnResults(data);
            String errorMsg = validateSet.stream()
                                         .map(ConstraintViolation::getMessage)
                                         .collect(Collectors.joining("&"));
            DemoSheet1Data dataErrorDTO = new DemoSheet1Data();
            dataErrorDTO.setValidateResult(errorMsg);
            cachedDataList.add(dataErrorDTO);
        }
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        ReadListener.super.onException(exception, context);
    }

    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        log.info("读取到了一条额外信息:{}", JSONUtil.toJsonStr(extra));
        switch (extra.getType()) {
            case COMMENT:
                log.info("额外信息是批注,在rowIndex:{},columnIndex;{},内容是:{}",
                         extra.getRowIndex(),
                         extra.getColumnIndex(),
                         extra.getText());
                break;
            case HYPERLINK:
                if ("Sheet1!A1".equals(extra.getText())) {
                    log.info("额外信息是超链接,在rowIndex:{},columnIndex;{},内容是:{}",
                             extra.getRowIndex(),
                             extra.getColumnIndex(),
                             extra.getText());
                } else if ("Sheet2!A1".equals(extra.getText())) {
                    log.info(
                        "额外信息是超链接,而且覆盖了一个区间,在firstRowIndex:{},firstColumnIndex;{},lastRowIndex:{},lastColumnIndex:{}," +
                        "内容是:{}",
                        extra.getFirstRowIndex(),
                        extra.getFirstColumnIndex(),
                        extra.getLastRowIndex(),
                        extra.getLastColumnIndex(),
                        extra.getText());
                } else {
                    log.error("Unknown hyperlink!");
                }
                break;
            case MERGE:
                log.info("额外信息是超链接,而且覆盖了一个区间,在firstRowIndex:{},firstColumnIndex;{},lastRowIndex:{},lastColumnIndex:{}",
                         extra.getFirstRowIndex(),
                         extra.getFirstColumnIndex(),
                         extra.getLastRowIndex(),
                         extra.getLastColumnIndex());
                break;
            default:
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (bValidate) {
            String writeFileName = MultiSheetsWriteTest.class.getResource("/").getPath() + "dynamicHeadValidateWrite" +
                                   System.currentTimeMillis() + ".xlsx";
            // 这里 指定文件
            try (ExcelWriter excelWriter = EasyExcel.write(writeFileName).build()) {
                WriteSheet writeSheet = EasyExcel.writerSheet(0, "模板0").head(DemoSheet1Data.class).build();
                excelWriter.write(cachedDataList, writeSheet);
            }
        }
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        log.info("存储数据库成功！");
    }
}
