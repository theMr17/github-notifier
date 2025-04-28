package com.notifier.app.auth.data.mappers

import com.notifier.app.auth.data.networking.dto.AuthTokenResponseDto
import com.notifier.app.auth.domain.AuthToken

fun AuthTokenResponseDto.toAuthToken() = AuthToken(
    accessToken = accessToken,
    scope = scope,
    tokenType = tokenType,
)
