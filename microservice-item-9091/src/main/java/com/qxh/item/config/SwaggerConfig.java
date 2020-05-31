package com.qxh.item.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2//开启swagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        //配置swagger 生成API的扫描范围
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().apis(
                RequestHandlerSelectors.basePackage("com.qxh.item")).paths(PathSelectors.any()).build();
    }
    /**
     * 创建api文档信息
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("强旭辉").description(
                "SpringCloud教程").termsOfServiceUrl("https://xxx").version("1.0").build();
    }
}
