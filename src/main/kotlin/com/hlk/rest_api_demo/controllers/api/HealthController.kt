package com.hlk.rest_api_demo.controllers.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class HealthController {
    @GetMapping("/health")
    fun health() : Health = Health.Alive
}

data class Health(val status: String) {
    companion object {
        val Alive = Health("Alive")
    }
}
