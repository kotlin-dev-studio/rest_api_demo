package com.hlk.rest_api_demo.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import kotlin.properties.Delegates

@Component
@ConfigurationProperties(prefix = "app")
@Validated
class AppProps {
    var version: String by Delegates.notNull()
    var message: String by Delegates.notNull()
}