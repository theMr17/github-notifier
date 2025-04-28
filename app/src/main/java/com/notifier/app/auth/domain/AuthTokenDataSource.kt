package com.notifier.app.auth.domain

import com.notifier.app.core.domain.util.NetworkError
import com.notifier.app.core.domain.util.Result

interface AuthTokenDataSource {
    suspend fun getAuthToken(
        clientId: String,
        clientSecret: String,
        code: String,
    ): Result<AuthToken, NetworkError>
}
