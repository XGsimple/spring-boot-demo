package com.xkcoding.excel.exceltool.dropdown.service.impl;

import com.xkcoding.excel.exceltool.dropdown.service.IDropDownService;

public class DropDownService implements IDropDownService {

    @Override
    public String[] getSource(String typeValue) {
        return new String[] {"研发部", "财务部", "人事部"};
    }
}
