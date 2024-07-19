package com.xkcoding.excel.exceltool.dict.enums;

public enum SexEnum {

    NAIL(1, "男"),
    FEMALE(2, "女"),
    UNKNOWN(3, "保密");

    private final Integer value;
    private final String describe;

    SexEnum(Integer value, String detail) {
        this.value = value;
        this.describe = detail;
    }

    public Integer getValue() {
        return value;
    }

    public String getDescribe() {
        return describe;
    }

    /**
     * 根据value获取describe
     *
     * @param value
     * @return
     */
    public static String getDescribe(Integer value) {
        for (SexEnum en : SexEnum.values()) {
            if (en.getValue().equals(value)) {
                return en.getDescribe();
            }
        }
        return "error";
    }

    /**
     * 根据describe获取value
     *
     * @param describe
     * @return
     */
    public static Integer getValue(String describe) {
        for (SexEnum en : SexEnum.values()) {
            if (en.getDescribe().equals(describe)) {
                return en.getValue();
            }
        }
        return -1;
    }

}
