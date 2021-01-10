package com.hlk.rest_api_demo.model

class ResultRes(
    val result: Boolean,
    val message: String
) {
    companion object {
        fun success(message: String = ""): ResultRes = ResultRes(true, message)

        fun failure(message: String = ""): ResultRes = ResultRes(false, message)
    }
}