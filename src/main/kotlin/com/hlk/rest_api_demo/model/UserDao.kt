package com.hlk.rest_api_demo.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
@Table(name = "users")
data class UserDao(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0,

    @Column
    var username: String? = null,

    @Column
    @JsonIgnore
    var password: String? = null
)
