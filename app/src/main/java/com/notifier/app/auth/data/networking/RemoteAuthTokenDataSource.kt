package com.notifier.app.auth.data.networking

import com.notifier.app.auth.data.mappers.toAuthToken
import com.notifier.app.auth.data.networking.dto.AuthTokenResponseDto
import com.notifier.app.auth.domain.AuthToken
import com.notifier.app.auth.domain.AuthTokenDataSource
import com.notifier.app.core.data.networking.safeCall
import com.notifier.app.core.domain.util.NetworkError
import com.notifier.app.core.domain.util.Result
import com.notifier.app.core.domain.util.map
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class RemoteAuthTokenDataSource @Inject constructor(
    private val httpClient: HttpClient,
) : AuthTokenDataSource {
    override suspend fun getAuthToken(
        clientId: String,
        clientSecret: String,
        code: String,
    ): Result<AuthToken, NetworkError> {
        return safeCall<AuthTokenResponseDto> {
            httpClient.get(
                urlString = "https://github.com/login/oauth/access_token"
            ) {
                parameter("client_id", clientId)
                parameter("client_secret", clientSecret)
                parameter("code", code)
            }
        }.map { response ->
            response.toAuthToken()
        }
    }
}
