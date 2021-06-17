package com.xkcoding.bf.json;

import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author xugangq
 * @description
 * @createTime 2021/3/6 14:56
 */
public class FastJsonHelper {

    public static String toJson(Object paramMap) {
        return toJson(paramMap, null);
    }

    public static String toJson(Object paramMap, String[] keys) {
        SerializeConfig mapping = new SerializeConfig();

        mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
        mapping.put(java.sql.Timestamp.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
        mapping.put(java.sql.Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
        mapping.put(Double.class, new DoubleSerializer("#.########"));
        mapping.put(BigDecimal.class, new BigDecimalCodec());

        PropertyFilter profilter = null;
        if (ArrayUtil.isNotEmpty(keys)) {
            List<String> exclusionKeys = Arrays.asList(keys);
            profilter = (object, name, value) -> {
                // false表示字段将被排除在外
                return !exclusionKeys.contains(name);
            };
        }
        return JSON.toJSONString(paramMap, mapping, profilter, SerializerFeature.WriteBigDecimalAsPlain);
    }

}
