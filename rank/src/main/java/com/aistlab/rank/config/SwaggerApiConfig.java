package com.aistlab.rank.config;


import lombok.Value;
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

import java.security.Timestamp;
import java.util.Date;

@Configuration
@EnableSwagger2
public class SwaggerApiConfig {
    public static final String BASE_PACKAGE = "com.aistlab";
    private boolean enableSwagger = true;
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())                // 生产环境的时候关闭 swagger 比较安全
                .enable(enableSwagger)                //将Timestamp类型全部转为Long类型
                .directModelSubstitute(Timestamp.class, Long.class)                //将Date类型全部转为Long类型
                .directModelSubstitute(Date.class, Long.class)
                .select()                // 扫描接口的包路径，不要忘记改成自己的
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build();
    }    private ApiInfo apiInfo() {        return new ApiInfoBuilder()
            .title("tank RESTful APIs")
            .description("Swagger API 服务")
            .termsOfServiceUrl("https://rank.sudoyc.com/")
            .contact(new Contact("ychost", "blog.sudoyc.com", "ychost@outlook.com"))
            .version("1.0")
            .build();
    }

}
