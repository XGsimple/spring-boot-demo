package com.xkcoding.orm.mybatis.plus.config;

import org.springframework.context.annotation.Configuration;

/**
 * @author MintLemon
 * @description
 * @createTime 2023-07-09 16:41
 */
@Configuration
public class MyFastJsonConfig {
    /**
     * 全局处理
     *
     * @JSONField(serialzeFeatures = SerializerFeature.WriteEnumUsingToString)
     * private StatusEnum status;
     */
    //    @Bean
    //    public FastJsonConfig fastJsonConfig() {
    //        FastJsonConfig config = new FastJsonConfig();
    //        config.setSerializerFeatures(SerializerFeature.WriteEnumUsingToString);
    //        return config;
    //    }

}
