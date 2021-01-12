package com.hlk.rest_api_demo.controller.sample.api.admin

import com.hlk.rest_api_demo.common.BearerHeaderToken
import com.hlk.rest_api_demo.model.Admin
import com.hlk.rest_api_demo.model.ResultDataRes
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/admin")
@RestController
class SampleAdminApiController {

    @BearerHeaderToken
    @GetMapping("/getAdmin")
    fun getAdmin(): ResponseEntity<ResultDataRes<Admin>> =
            ResponseEntity.ok(ResultDataRes(Admin("Admin", "1234")))
}
