package com.xkcoding.excel.multiwrite.enums;

import com.xkcoding.common.enums.EnumCache;
import com.xkcoding.common.enums.InnerEnum;

/**
 * @Author: ZhangCheng
 * @Date: 2024/3/7
 */
public enum Module implements InnerEnum.StrCodeEnum {
    TEST("TEST", "测试"),

    ///////////////////////////////////集团综述1/////////////////////////////////
    WHOLE_SALE("WHOLE_SALE", "整体销售"),
    OFF_LINE("OFF_LINE", "线下"),
    ON_LINE_O2O("ON_LINE_O2O", "线上O2O"),
    ON_LINE_B2C("ON_LINE_B2C", "线上B2C"),
    TRANSACTION_ORDER("TRANSACTION_ORDER", "交易客单"),
    NEW_OLD_STORE("NEW_OLD_STORE", "新老门店"),
    FOUR_CATEGORIES("FOUR_CATEGORIES", "四大品类"),
    /**********************************集团综述2-start****************************************/
    GROUP_TWO_FJK("GROUP_TWO_FJK", "泛健康"),
    GROUP_TWO_BUSI("GROUP_TWO_BUSI", "服务类业务"),
    /**********************************集团综述2-end****************************************/

    ///////////////////////////////////集团综述4/////////////////////////////////
    INVENTORY_TURNOVER("INVENTORY_TURNOVER", "库存周转"),
    ARRIVAL_RATE("ARRIVAL_RATE", "到货率"),
    LARGE_SCALE_ACTIVITY("LARGE_SCALE_ACTIVITY", "大型活动"),
    MEMBER("MEMBER", "会员"),
    ///////////////////////////////////集团综述3/////////////////////////////////
    PURCHASE_AND_SALE_AMOUNT("PURCHASE_AND_SALE_AMOUNT", "进销差额"),
    PURCHASE_AND_SALE_RATIO("PURCHASE_AND_SALE_RATIO", "进销差率"),
    DISCOUNT_RATE("DISCOUNT_RATE", "折扣率"),
    MAINLY_POPULARIZE("MAINLY_POPULARIZE", "主推"),
    E_COMMERCE_BUSINESS("E_COMMERCE_BUSINESS", "电商业务"),
    ///////////////////////////////////集团综述5/////////////////////////////////
    STAR_COMMODITY("STAR_COMMODITY", "心耀商品"),
    STORES_DEVELOP("STORES_DEVELOP", "门店发展"),
    MERCHANTS("MERCHANTS", "加盟店"),
    YX_CONVENIENCE("YX_CONVENIENCE", "一心便利"),

    ///////////////////////////////////子公司经营分述/////////////////////////////////
    COMP_YUN_NAN("COM_YUN_NAN", "云南"),
    COMP_GUANG_XI("COM_GUANG_XI", "广西"),
    COMP_GUI_ZHOU("COM_GUI_ZHOU", "贵州"),
    COMP_SHAN_XI("COM_SHAN_XI", "山西"),
    COMP_SI_CHUAN("COM_SI_CHUAN", "四川"),
    COMP_CHONG_QING("COM_CHONG_QING", "重庆"),
    COMP_HAI_NAN("COM_HAI_NAN", "海南"),

    ;

    private final InnerEnum<String, String> innerEnum;

    Module(String code, String msg) {
        innerEnum = new InnerEnum<>(code, msg);
    }

    @Override
    public InnerEnum<String, String> get() {
        return innerEnum;
    }

    static {
        EnumCache.registerByValue(Module.class, Module.values(), Module::getCode);
    }
}
