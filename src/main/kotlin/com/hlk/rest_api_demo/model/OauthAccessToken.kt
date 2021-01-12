package com.hlk.rest_api_demo.model

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "oauth_access_tokens")
class OauthAccessToken(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @Column(name = "resource_owner")
        var resource_owner: Long? = null,

        @Column(name = "access_token", length = 255, nullable = false)
        var access_token: String? = null,

        @Column(name = "refresh_token", length = 255, nullable = true)
        var refresh_token: String? = null,

        @Column(name = "expires_in")
        var expires_in: Int? = null,

        @Column(name = "token_type")
        var token_type: String = ""
) : Serializable
