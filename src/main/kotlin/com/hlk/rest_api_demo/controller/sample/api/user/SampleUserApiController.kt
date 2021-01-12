package com.hlk.rest_api_demo.controller.sample.api.user

import com.hlk.rest_api_demo.common.BearerHeaderToken
import com.hlk.rest_api_demo.model.ResultDataRes
import com.hlk.rest_api_demo.model.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/user")
@RestController
@BearerHeaderToken
class SampleUserApiController {
    @GetMapping("/getUser")
    fun getUser(): ResponseEntity<ResultDataRes<User>> =
        ResponseEntity.ok(ResultDataRes(User(1, "username", "email")))
}
