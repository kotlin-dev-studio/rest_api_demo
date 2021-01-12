package com.hlk.rest_api_demo.common.security

import org.springframework.security.config.annotation.web.HttpSecurityBuilder
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer

fun <T : HttpSecurityBuilder<T>> ExpressionUrlAuthorizationConfigurer<T>.AuthorizedUrl.hasRole(role: Enum<*>): ExpressionUrlAuthorizationConfigurer<T>.ExpressionInterceptUrlRegistry =
        hasRole(role.toString())
