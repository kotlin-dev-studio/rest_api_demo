package com.hlk.rest_api_demo.common

import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams

@ApiImplicitParams(
    ApiImplicitParam(
        name = "Authorization",
        value = "Access Token",
        required = true,
        allowEmptyValue = false,
        paramType = "header",
        dataTypeClass = String::class,
        defaultValue = "Bearer"
    )
)
annotation class BearerHeaderToken {}
