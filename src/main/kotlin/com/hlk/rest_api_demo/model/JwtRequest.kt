package com.hlk.rest_api_demo.model

import java.io.Serializable

class JwtRequest : Serializable {
    var username: String? = null
    var password: String? = null

    private val serialVersionUID = 5926468583005150707L
}
