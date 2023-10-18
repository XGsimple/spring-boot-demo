package com.xkcoding.swagger.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Swagger3配置
 * https://juejin.cn/post/7078109157656231944#heading-7
 * <p>
 * 访问地址：http://localhost:8080/demo/swagger-ui/index.html
 *
 * @Author XuGang
 * @Date 2023/10/18 16:32
 */
@EnableOpenApi
@Configuration
public class Swagger3Config {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)// v2 不同
                                                   .apiInfo(apiInfo()).select()
                                                   //RequestHandlerSelectors配置要扫描接口的方式
                                                   //         basePackage:指定要扫描的包=>RequestHandlerSelectors.basePackage("com.swagger.controller")
                                                   //         any():扫描全部
                                                   //         none():全部不扫描
                                                   //         withClassAnnotation:扫描类上的注解=>RequestHandlerSelectors.withClassAnnotation(RestController.class)
                                                   //         withMethodAnnotation:扫描方法上的注解=>RequestHandlerSelectors.withMethodAnnotation(GetMapping.class)
                                                   .apis(RequestHandlerSelectors.basePackage(
                                                       "com.xkcoding.swagger.controller"))// 设置扫描路径
                                                   .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("spring-boot-demo")
                                   .description("这是一个简单的 Swagger API 演示")
                                   .contact(new Contact("Yangkai.Shen", "http://xkcoding.com", "237497819@qq.com"))
                                   .version("1.0.0-SNAPSHOT")
                                   .build();
    }

    /**
     * 增加如下配置可解决Spring Boot 6.x 与Swagger 3.0.0 不兼容问题
     **/
    @Bean
    public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
                    customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
                }
                return bean;
            }

            private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(
                List<T> mappings) {
                List<T> copy = mappings.stream()
                                       .filter(mapping -> mapping.getPatternParser() == null)
                                       .collect(Collectors.toList());
                mappings.clear();
                mappings.addAll(copy);
            }

            @SuppressWarnings("unchecked")
            private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
                try {
                    Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
                    field.setAccessible(true);
                    return (List<RequestMappingInfoHandlerMapping>)field.get(bean);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
        };
    }
}
