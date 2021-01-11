package com.hlk.rest_api_demo.model

import javax.persistence.*

@Entity
@Table(name = "users")
class User {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "Username", length = 255, nullable = false)
    var username: String? = null

    @Column(name = "password", length = 255, nullable = false)
    var password: String? = null

    @Column(name = "role", length = 255, nullable = true)
    var role: String? = null
}
