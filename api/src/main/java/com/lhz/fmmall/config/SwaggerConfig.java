package com.lhz.fmmall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /*swagger会帮助我们生成接口文档
    * 1：配置生成文档信息
    * 2：配置生成规则
    */

    /* Docket 封装接口文档信息*/

    @Bean
    public Docket getDocket(){

        //创建封面信息对象
        ApiInfoBuilder apiInfoBuilder = new ApiInfoBuilder();
        apiInfoBuilder.title("《锋迷商城》后端接口说明")
                .description("此文档详细说明了锋迷商城项目后端接口规范.....")
                .version("v 2.0.1")
                .contact(new Contact("rong","www.rong.com","rong@rong.com"));
        ApiInfo apiInfo = apiInfoBuilder.build();

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lhz.fmmall.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}
