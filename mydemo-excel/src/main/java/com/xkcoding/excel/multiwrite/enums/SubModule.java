package com.xkcoding.excel.multiwrite.enums;

import com.xkcoding.common.enums.EnumCache;
import com.xkcoding.common.enums.InnerEnum;

/**
 * @Author: ZhangCheng
 * @Date: 2024/3/7
 */
public enum SubModule implements InnerEnum.StrCodeEnum {

    ///////////////////////////////////子公司分述/////////////////////////////////
    RED("RED", "红榜"),
    DARK("DARK", "黑榜"),
    DEPT_RED("DEPT_RED", "分部红榜"),
    DEPT_DARK("DEPT_DARK", "分部黑榜"),

    ///////////////////////////////////整体销售/////////////////////////////////
    SALES_TASK_COMPLETED("SALES_TASK_COMPLETED", "销售任务达成"),
    COMPLETE_SALES_TASKS_CUMULATIVELY("COMPLETE_SALES_TASKS_CUMULATIVELY", "累计销售任务达成"),
    SALES_TASK_COMPLETED_RETAIL("SALES_TASK_COMPLETED_RETAIL", "销售任务达成-零售"),
    SALES_TASK_COMPLETED_OTHER_BUSINESS("SALES_TASK_COMPLETED_OTHER_BUSINESS", "销售任务达成-其他业务"),
    MISSION_DIVISION_WAS_NOT_REACHED("MISSION_DIVISION_WAS_NOT_REACHED", "未达成任务分部"),
    QUEST_ACHIEVEMENT_RATE_BLACKLIST_DIVISION("QUEST_ACHIEVEMENT_RATE_BLACKLIST_DIVISION", "任务达成率黑榜-分部"),

    ///////////////////////////////////新老门店/////////////////////////////////
    PER_CUSTOMER_TRANSACTION("PER_CUSTOMER_TRANSACTION", "客单价"),
    UNIT_PRICE_MEDICAL_INSURANCE_CUSTOMERS("UNIT_PRICE_MEDICAL_INSURANCE_CUSTOMERS", "医保客单价"),
    PER_CUSTOMER_TRANSACTION_COMP("PER_CUSTOMER_TRANSACTION_COMP", "客单价-子公司"),
    NUMBER_TRADES_YOY_COMP("NUMBER_TRADES_YOY_COMP", "交易次数同比-子公司"),

    ///////////////////////////////////交易客单/////////////////////////////////
    OLD_STORE("OLD_STORE", "老店"),
    NEW_STORE("NEW_STORE", "新店"),
    MERGER_STORE("MERGER_STORE", "并购店"),
    TASK_COMPLETED_COMP("TASK_COMPLETED_COMP", "任务达成率-子公司"),

    ///////////////////////////////////四大品类/////////////////////////////////

    ///////////////////////////////////库存周转/////////////////////////////////
    GROUP_TURNOVER_DAYS("GROUP_TURNOVER_DAYS", "集团库存周转天数"),

    SUB_TURNOVER_DAYS("SUBSIDIARY_TURNOVER_DAYS", "周转天数-子公司"),

    INVENTORY_TOP("INVENTORY_TOP", "库存周转天数TOP-5"),

    INVENTORY_BOTTOM("INVENTORY_BOTTOM", "库存周转天数BOTTOM-5"),

    ///////////////////////////////////到货率/////////////////////////////////
    SUB_ARRIVAL_RATE("SUB_ARRIVAL_RATE", "到货率-子公司"),

    ///////////////////////////////////大型活动/////////////////////////////////
    ACTIVITY_TASK_COMPLETION_RATE("ACTIVITY_TASK_COMPLETION_RATE", "任务达成率"),
    PROMOTE_RATE("PROMOTE_RATE", "实际提升率"),
    DISCOUNT_RATE("DISCOUNT_RATE", "折扣率"),
    ACTIVITY_SALES_DIFFERENCE_RATE("ACTIVITY_SALES_DIFFERENCE_RATE", "进销差率"),
    SUB_TASK_COMPLETION("SUB_TASK_COMPLETION", "任务达成-子公司"),
    TASK_COMPLETION_RED_LIST("TASK_COMPLETION_RED_LIST", "任务达成红榜-分部"),
    TASK_COMPLETION_BLACK_LIST("TASK_COMPLETION_BLACK_LIST", "任务达成黑榜-分部"),

    ///////////////////////////////////会员/////////////////////////////////
    VALID_MEMBER_COUNT("VALID_MEMBER_COUNT", "当月有效会员数"),
    SUB_MEMBER_ATTENDANCE_RATE("SUB_MEMBER_ATTENDANCE_RATE", "会员到店率-子公司"),
    SUB_MEMBER_REPURCHASE_RATE("SUB_MEMBER_REPURCHASE_RATE", "会员复购率-子公司"),
    SUB_MEMBER_CHURN_RATE("SUB_MEMBER_CHURN_RATE", "会员流失率-子公司"),
    ///////////////////////////////////心耀商品/////////////////////////////////
    STAR_TASK_COMPLETION_RATE("STAR_TASK_COMPLETION_RATE", "任务达成率"),
    STAR_SALES_DIFFERENCE_RATE("STAR_SALES_DIFFERENCE_RATE", "进销差率"),
    OWNERSHIP_RATE("OWNERSHIP_RATE", "保有率"),
    DYNAMIC_SALES_RATE("DYNAMIC_SALES_RATE", "动销率"),
    SUB_TASK_SALES("SUB_TASK_SALES", "任务达成&进销差率-子公司"),
    SUB_OWNERSHIP_DYNAMIC("SUB_OWNERSHIP_DYNAMIC", "保有率&动态率-子公司"),
    ///////////////////////////////////门店发展/////////////////////////////////
    NEW_STORE_COMPLETION_RATE("NEW_STORE_COMPLETION_RATE", "新店-累计开店达成率"),
    MA_STORE_COMPLETION_RATE("MA_STORE_COMPLETION_RATE", "并购店-累计开店达成率"),
    SUB_STORE_NET_GROWTH_COMPLETION_RATE("SUB_STORE_NET_GROWTH_COMPLETION_RATE", "新开净增长门店及开店达成率-子公司"),
    SUB_MA_STORE_NET_GROWTH("SUB_MA_STORE_NET_GROWTH", "并购净增长门店-子公司"),
    ///////////////////////////////////加盟店/////////////////////////////////
    STORE_START_COUNT("STORE_START_COUNT", "门店开业数"),
    STORE_AGENCY_COUNT("STORE_AGENCY_COUNT", "签约门店数"),
    START_COMPLETION_RATE("START_COMPLETION_RATE", "开业完成率"),
    AGENCY_COMPLETION_RATE("AGENCY_COMPLETION_RATE", "签约完成率"),
    START_CUMULATIVE_COUNT("START_CUMULATIVE_COUNT", "累计开业门店数"),
    STORE_PREPARE_COUNT("STORE_PREPARE_COUNT", "筹建门店数"),
    SUB_STAGE_STORE_COUNT("SUB_STAGE_STORE_COUNT", "各阶段门店统计-子公司"),
    SUB_START_CUMULATIVE_COUNT("SUB_START_CUMULATIVE_COUNT", "累计开业门店-子公司"),
    ///////////////////////////////////一心便利/////////////////////////////////
    RETAIL_SALE_COMPLETION_RATE("RETAIL_SALE_COMPLETION_RATE", "零售销售完成率"),
    WHOLE_SALE_COMPLETION_RATE("WHOLE_SALE_COMPLETION_RATE", "批发销售完成率"),
    RETAIL_SALE_DIFFERENCE_COMPLETION_RATE("RETAIL_SALE_DIFFERENCE_COMPLETION_RATE", "零售进销差完成率"),
    RETAIL_SALE_DIFFERENCE_RATE("RETAIL_SALE_DIFFERENCE_RATE", "零售进销差率"),
    STORE_AVERAGE_DAILY_DEAL_TREND("STORE_AVERAGE_DAILY_DEAL_TREND", "店日均交易次数趋势"),
    PER_CUSTOMER_TREND("PER_CUSTOMER_TREND", "客单价趋势"),
    ///////////////////////////////////电商业务/////////////////////////////////
    E_SALES_TASK_COMPLETED("E_SALES_TASK_COMPLETED", "销售任务达成"),
    E_COMPLETE_SALES_TASKS_CUMULATIVELY("E_COMPLETE_SALES_TASKS_CUMULATIVELY", "累计销售任务达成"),
    E_GPR("E_GPR", "毛利率"),
    E_ADD_UP_GPR("E_ADD_UP_GPR", "累计毛利率"),
    E_TYPE_SALES_TASK_COMPLETED_SALES_YOY_GPR("E_TYPE_SALES_TASK_COMPLETED_SALES_YOY_GPR",
                                              "多类型销售任务达成、销售额同比、毛利率"),
    /**********************************集团综述2-start****************************************/
    GROUP_TWO_FJK_CATEGORY_PROPORTION("GROUP_TWO_FJK_CATEGORY_PROPORTION", "泛健康品类-各品类销售占比"),
    GROUP_TWO_BUSI_BUSINESS_OVERVIEW("GROUP_TWO_BUSI_BUSINESS_OVERVIEW", "服务类业务总览"),
    GROUP_TWO_BUSI_BUSINESS_PROPORTION("GROUP_TWO_BUSI_BUSINESS_PROPORTION", "服务类业务-各品类销售占比"),
    GROUP_TWO_BUSI_BUSINESS_COMPANY_TASK_RATE("GROUP_TWO_BUSI_BUSINESS_COMPANY_TASK_RATE",
                                              "服务类业务-彩票销售任务达成-子公司"),
    GROUP_TWO_BUSI_BUSINESS_LAST_YEAR_SALE("GROUP_TWO_BUSI_BUSINESS_LAST_YEAR_SALE", "服务类业务-近一年销售占比趋势"),
    /**********************************集团综述2-end****************************************/

    ;

    private final InnerEnum<String, String> innerEnum;

    SubModule(String code, String msg) {
        innerEnum = new InnerEnum<>(code, msg);
    }

    @Override
    public InnerEnum<String, String> get() {
        return innerEnum;
    }

    static {
        EnumCache.registerByValue(SubModule.class, SubModule.values(), SubModule::getCode);
    }
}
