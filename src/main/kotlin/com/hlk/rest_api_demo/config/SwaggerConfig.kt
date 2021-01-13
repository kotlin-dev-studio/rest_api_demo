package com.hlk.rest_api_demo.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiKey
import springfox.documentation.service.SecurityScheme
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

@Configuration
class SwaggerConfig {

    @Bean
    fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
        .produces(HashSet(listOf("application/json")))
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(
            PathSelectors.ant("/api/**")
                .or(PathSelectors.ant("/login")
                    .or(PathSelectors.ant("/signup")
                        .or(PathSelectors.ant("/sample/**")))))
        .build()
        .securitySchemes(listOf(apiKey()) as List<SecurityScheme>?)
    private fun apiKey(): ApiKey? {
        return ApiKey("Authorization", "Authorization", "header");
    }
}
