package com.idc.idc.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api(@Value("${swagger.pathMapping}") String swaggerPathMapping,
                      @Value("${swagger.host}") String swaggerHost) {
        Docket api = new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.idc"))
                .build()
                .ignoredParameterTypes(AuthenticationPrincipal.class)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo());

        if (StringUtils.isNotBlank(swaggerHost)) {
            api.host(swaggerHost);
        }

        if (StringUtils.isNotBlank(swaggerPathMapping)) {
            api.pathMapping(swaggerPathMapping);
        }

        return api;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("IDC REST API")
                .version("v0")
                .build();
    }
}
