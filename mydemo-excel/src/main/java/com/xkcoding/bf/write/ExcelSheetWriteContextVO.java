package com.xkcoding.bf.write;

import com.alibaba.excel.write.handler.WriteHandler;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class ExcelSheetWriteContextVO {
    /**
     * sheet页号
     */
    private Integer sheetIndex;

    /**
     * sheet名称
     */
    private String sheetName;

    /**
     * 数据
     */
    private List<ExcelWriteTableVO> tables;

    /**
     * 样式
     */
    private List<WriteHandler> handlers;

    /**
     * head 参数
     */
    private Map<String, String> vars;
}
