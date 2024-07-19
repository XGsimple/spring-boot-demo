package com.xkcoding.common.constants;

/**
 * 异常返回信息msg常量
 *
 * @Author XuGang
 * @Date 2023/11/6 16:50
 */
public interface RespMsgConstants {
    /**
     * 通用返回
     */
    interface Common {

        String SORT_ITEM_NULL_ERROR = "参数错误：排序项为空";

        String SORT_ITEM_FORMAT_ERROR = "参数错误：排序项格式错误";

    }

    /**
     * 一心助手
     */
    interface Assist {
        //门店趋势
        String PARA_ERROR_STORE_TREAD_CATEGORY = "参数错误：销售数据趋势分类";
        String PARA_ERROR_STORE_TREAD_DATA = "参数错误：销售数据趋势分类";
        String PARA_ERROR_COMMODITYCODE_BARCODE_SYNC_IS_NULL = "参数错误：商品编码与69条形码不能同时为空";
        String PARA_ERROR_DATE_ERROR = "参数错误：时间传参错误";
        String BIZ_ERROR_STRATEGY_ERROR = "无相应策略类";
        String OVERCOUNTED = "参数错误:条数过多";
    }
}