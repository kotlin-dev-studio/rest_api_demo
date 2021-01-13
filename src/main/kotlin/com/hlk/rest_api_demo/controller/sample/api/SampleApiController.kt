package com.hlk.rest_api_demo.controller.sample.api

import com.hlk.rest_api_demo.common.BearerHeaderToken
import com.hlk.rest_api_demo.config.AppProps
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api")
@RestController
@BearerHeaderToken
class SampleApiController(
    private val appProps: AppProps
) {
    @GetMapping("/appProps")
    fun appProps() = appProps
}
