package com.hlk.rest_api_demo.model

class ResultDataRes<out T>(
    val data: T? = null,
    val message: String? = null
)
