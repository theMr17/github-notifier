package com.notifier.app.core.data.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Factory object for creating an instance of [HttpClient] with predefined configurations.
 */
object HttpClientFactory {
    /**
     * Creates and configures an instance of [HttpClient] with logging, JSON serialization,
     * and default request headers.
     *
     * @param engine The HTTP client engine to use for network requests.
     * @return A configured instance of [HttpClient].
     */
    fun create(engine: HttpClientEngine): HttpClient = HttpClient(engine) {
        // Enable logging for network requests and responses
        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.ALL
        }

        // Configure request timeouts
        install(HttpTimeout) {
            requestTimeoutMillis = 30000  // 30 seconds
            connectTimeoutMillis = 15000  // 15 seconds
            socketTimeoutMillis = 15000   // 15 seconds
        }

        // Configure JSON serialization/deserialization
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }

        // Set default request headers and properties
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }
}
