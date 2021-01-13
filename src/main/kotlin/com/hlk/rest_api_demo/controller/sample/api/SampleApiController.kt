package com.hlk.rest_api_demo.controller.sample.api

import com.hlk.rest_api_demo.common.BearerHeaderToken
import com.hlk.rest_api_demo.config.AppProps
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.MediaType

@RequestMapping("/api")
@RestController
@BearerHeaderToken
class SampleApiController {
    private lateinit var appProps: AppProps

    @GetMapping("/appProps")
    fun appProps() = appProps
}
