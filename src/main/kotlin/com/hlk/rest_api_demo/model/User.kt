package com.hlk.rest_api_demo.model

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name ="users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @Column(name = "username", length = 255, nullable = false)
    var username: String? = null,

    @Column(name = "email", length = 255, nullable = true)
    var email: String? = null,

    @Column(name = "password", length = 255, nullable = false)
    var password: String? = null,

    @Column(name = "role", length = 255, nullable = true)
    var role: String? = ""
) : Serializable
