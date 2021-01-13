package com.hlk.rest_api_demo.controller.sample.api.authen

import com.hlk.rest_api_demo.common.BearerHeaderToken
import com.hlk.rest_api_demo.config.AppProps
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/authen")
@RestController
@BearerHeaderToken
class SampleAuthenApiController(
    private val appProps: AppProps
) {
    @GetMapping("/appProps")
    fun appProps() = appProps
}

// https://github.com/spring-projects/spring-security-oauth2-boot/blob/master/samples/spring-boot-sample-secure-oauth2-provider/src/test/java/sample/SampleSecureOAuth2ApplicationTests.java
