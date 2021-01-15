package com.hlk.rest_api_demo.controllers.sample

import com.hlk.rest_api_demo.common.annotation.BearerHeaderToken
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.Serializable

class Health(
    val status: String
) : Serializable

@RestController
@BearerHeaderToken
@RequestMapping("/sample")
class SampleApiController {

    @GetMapping("/health")
    fun health(): ResponseEntity<Health> {
        return ResponseEntity.ok(Health("Alive"))
    }
}
