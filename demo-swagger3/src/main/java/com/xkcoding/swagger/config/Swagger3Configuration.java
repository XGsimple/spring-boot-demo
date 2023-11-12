package com.xkcoding.swagger.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.Api;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableOpenApi
@EnableKnife4j
@Slf4j
public class Swagger3Configuration {

    @Value("${spring.profiles.active:''}")
    private String active;
    @Value("${server.port}")
    private int port;
    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Bean
    @SneakyThrows
    public Docket shopManager() {
        Docket docket = new Docket(DocumentationType.OAS_30)
            // 非生产环境才开启swagger
            .enable(!active.equalsIgnoreCase("prod"))
            .groupName("group1")
            .apiInfo(apiInfo())
            // 设置自定义返回消息体
            .globalResponses(HttpMethod.GET, globalResponse())
            .globalResponses(HttpMethod.POST, globalResponse())
            .select()
            // 扫描带有@Api注解的类
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            // 扫描的基础路径 any()：任何请求都扫描；none()：任何请求都不扫描；ant("/manager")：扫描该路径下的请求
            .paths(PathSelectors.any())
            .build();

        String ipAddress =
            !active.equalsIgnoreCase("local") ? InetAddress.getLocalHost().getHostAddress() : "localhost";
        // 控制台输出Swagger3接口文档地址
        log.info("Swagger3接口文档地址: http://{}:{}{}/swagger-ui/index.html", ipAddress, port, contextPath);
        // 控制台输出Knife4j增强接口文档地址
        log.info("Knife4j增强接口文档地址: http://{}:{}{}/doc.html", ipAddress, port, contextPath);
        return docket;
    }

    private ApiInfo apiInfo() {
        String title = "XXX系统";
        String description = "API接口文档";
        String version = "1.0.0";
        String license = "";
        String termsOfServiceUrl = "";
        Contact contact = new Contact("JustryDeng", "https://gitee.com/JustryDeng", "13548417409@163.com");
        return new ApiInfoBuilder().title(title)
                                   .description(description)
                                   .version(version)
                                   .license(license)
                                   .termsOfServiceUrl(termsOfServiceUrl)
                                   .contact(contact)
                                   .build();
    }

    private List<Response> globalResponse() {
        List<Response> responseList = new ArrayList<>();
        responseList.add(new ResponseBuilder().code("401").description("未认证").build());
        responseList.add(new ResponseBuilder().code("403").description("请求被禁止").build());
        responseList.add(new ResponseBuilder().code("404").description("找不到资源").build());
        return responseList;
    }

    /**
     * 增加如下配置可解决Spring Boot 6.x 与Swagger 3.0.0 不兼容问题
     **/
    //@Bean
    //public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
    //    return new BeanPostProcessor() {
    //
    //        @Override
    //        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    //            if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
    //                customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
    //            }
    //            return bean;
    //        }
    //
    //        private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(
    //            List<T> mappings) {
    //            List<T> copy = mappings.stream()
    //                                   .filter(mapping -> mapping.getPatternParser() == null)
    //                                   .collect(Collectors.toList());
    //            mappings.clear();
    //            mappings.addAll(copy);
    //        }
    //
    //        @SuppressWarnings("unchecked")
    //        private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
    //            try {
    //                Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
    //                field.setAccessible(true);
    //                return (List<RequestMappingInfoHandlerMapping>)field.get(bean);
    //            } catch (IllegalArgumentException | IllegalAccessException e) {
    //                throw new IllegalStateException(e);
    //            }
    //        }
    //    };
    //}

}
