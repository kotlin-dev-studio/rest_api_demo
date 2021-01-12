package com.hlk.rest_api_demo.repository

import com.hlk.rest_api_demo.model.OauthAccessToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccessTokenRepository : JpaRepository<OauthAccessToken?, Long?>
