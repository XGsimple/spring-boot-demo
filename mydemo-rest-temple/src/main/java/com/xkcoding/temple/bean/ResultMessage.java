package com.xkcoding.temple.bean;

import com.alibaba.fastjson.JSON;
import com.xkcoding.temple.json.FastJsonHelper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author xugangq
 * @description
 * @createTime 2021/3/6 14:24
 */
@Data
@NoArgsConstructor
public class ResultMessage {
    private static final String DATEFORMATE = "yyyy-MM-dd HH:mm:ss";

    private Number code;
    private String message;
    private Object data;

    public ResultMessage(Number code, String message, Object data) {
        if (message != null) {
            message = message.replaceFirst("^.*Exception:\\s*", "");
        }
        if (StringUtils.isBlank(message)) {
            message = "服务端逻辑异常" /* "操作成功" */;
        }
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static String data(Object data, String[] keys) {
        return getJsonResult(200, "操作成功", data, keys);
    }

    public static String data(Object data, boolean isHoldNull) {
        if (isHoldNull) {
            return holdNulldata(data);
        } else {
            return data(data, null);
        }
    }

    public static String data(Object data) {
        return data(data, null);
    }


    /**
     * 保留null值序列化
     *
     * @param data
     * @return
     */
    private static String holdNulldata(Object data) {
        ResultMessage result = new ResultMessage(200, "操作成功", data);
        return FastJsonHelper.toJson(result);
    }

    @SuppressWarnings("rawtypes")
    public static String toMap(String data) {
        return data((Map) JSON.parseObject(data, Map.class), null);
    }

    @SuppressWarnings("rawtypes")
    public static String toMap(String data, boolean isHoldNull) {
        return data((Map) JSON.parseObject(data, Map.class), isHoldNull);
    }
    /*
    public static String toData(Object data){
		Object ret=null;
		if(data!=null){
			ret=((BizObject)data).__data();;
		}
		return data(ret,null);
	}
	*/

    /**
     * 对时间日期格式化 并序列化为JSON
     */
    public static String formatDate(Object data) {
        return formatDate(data, DATEFORMATE);
    }

    /**
     * 对时间日期格式化 并序列化为JSON
     */
    public static String formatDate(Object data, String dateFormat) {
        return formatDate(data, dateFormat, null);
    }

    /**
     * 对时间日期格式化 并序列化为JSON
     */
    public static String formatDate(Object data, String[] keys) {
        return formatDate(data, DATEFORMATE, keys);
    }

    public static String formatDate(Object data, String dateFormate, String[] keys) {
        return getJsonResult(200, "操作成功" /* "操作成功" */, data, keys);
    }


    private static String getJsonResult(Number code, String message, Object data, String[] keys) {
        ResultMessage result = new ResultMessage(code, message, data);
        return getJsonResult(result, keys);
    }

    private static String getJsonResult(ResultMessage result, String[] keys) {
        return FastJsonHelper.toJson(result, keys);
    }
}
