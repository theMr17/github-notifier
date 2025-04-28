package com.notifier.app.auth.domain

data class AuthToken(
    val accessToken: String,
    val scope: String,
    val tokenType: String,
)
