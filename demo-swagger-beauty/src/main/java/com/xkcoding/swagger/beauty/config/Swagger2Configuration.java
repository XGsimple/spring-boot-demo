package com.xkcoding.swagger.beauty.config;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Swagger2Configuration
 * 方便继承，构建
 *
 * @Author XuGang
 * @Date 2024/08/14 14:41:43
 **/
@Configuration
@EnableSwagger2
public class Swagger2Configuration {

    @NoArgsConstructor
    public static class DocketBuilder {

        private String groupName;
        private String title;
        private String description;
        private String[] basePackages;

        public DocketBuilder basePackages(String... basePackages) {
            this.basePackages = basePackages;
            return this;
        }

        public DocketBuilder groupName(String groupName) {
            this.groupName = groupName;
            return this;
        }

        public DocketBuilder title(String title) {
            this.title = title;
            return this;
        }

        public DocketBuilder description(String description) {
            this.description = description;
            return this;
        }

        public Docket newDocket(Boolean enable) {
            List<Predicate<RequestHandler>> collect = Lists.newArrayList(this.basePackages)
                                                           .stream()
                                                           .map(RequestHandlerSelectors::basePackage)
                                                           .collect(Collectors.toList());

            return new Docket(DocumentationType.SWAGGER_2).groupName(this.groupName)
                                                          .apiInfo(apiInfo(this.title, this.description))
                                                          .securityContexts(Lists.newArrayList(securityContext()))
                                                          .securitySchemes(Lists.newArrayList(new ApiKey("JWT",
                                                                                                         "Authorization",
                                                                                                         "header"),
                                                                                              new ApiKey("员工编号",
                                                                                                         "empCode",
                                                                                                         "header"),
                                                                                              new ApiKey("用户",
                                                                                                         "userId",
                                                                                                         "header")))
                                                          .enable(enable)
                                                          .select()
                                                          .apis(Predicates.or(collect))
                                                          .paths(PathSelectors.any())
                                                          .build();
        }

        private ApiInfo apiInfo(String title, String description) {
            return new ApiInfoBuilder().title(title).description(description).contact(new Contact("", "", "")).build();
        }

        private SecurityContext securityContext() {
            AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
            AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
            authorizationScopes[0] = authorizationScope;
            List<SecurityReference> defaultAuth = Collections.singletonList(new SecurityReference("JWT",
                                                                                                  authorizationScopes));

            return SecurityContext.builder().securityReferences(defaultAuth).build();
        }
    }
}
