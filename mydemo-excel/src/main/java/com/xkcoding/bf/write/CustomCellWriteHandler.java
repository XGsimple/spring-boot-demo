package com.xkcoding.bf.write;

import com.alibaba.excel.write.style.row.AbstractRowHeightStyleStrategy;
import org.apache.poi.ss.usermodel.Row;

public class CustomCellWriteHandler extends AbstractRowHeightStyleStrategy {

    @Override
    protected void setHeadColumnHeight(Row row, int i) {
        if (i == 0) {
            row.setHeight((short)(1000));
        } else if (i == 1) {
            row.setHeight((short)300);
        } else {
            row.setHeight((short)500);
        }
    }

    @Override
    protected void setContentColumnHeight(Row row, int i) {
        row.setHeight((short)500);
    }
}
