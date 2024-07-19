package com.xkcoding.excel.exceltool.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.xkcoding.excel.exceltool.dict.enums.SexEnum;

/**
 * @program: mypro
 * @description: todo
 * @author: xuYao2
 * @create: 2022-07-22 11:10
 **/
public class SexConverter implements Converter<Integer> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return Integer.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Integer convertToJavaData(ReadConverterContext<?> context) throws Exception {
        return SexEnum.getValue(context.getReadCellData().getStringValue());
    }

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<Integer> context) throws Exception {
        return new WriteCellData<String>(SexEnum.getDescribe(context.getValue()));
    }
}
