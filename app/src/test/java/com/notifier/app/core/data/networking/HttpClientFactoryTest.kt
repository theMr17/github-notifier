package com.notifier.app.core.data.networking

import com.google.common.truth.Truth.assertThat
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import org.junit.Test

class HttpClientFactoryTest {
    @Test
    fun testHttpClientFactory_createsHttpClientSuccessfully() {
        val client = createMockClient()

        assertThat(client).isInstanceOf(HttpClient::class.java)
    }

    @Test
    fun testHttpClientFactory_loggingIsInstalled() {
        val client = createMockClient()

        val isLoggingInstalled = try {
            client.plugin(Logging)
            true
        } catch (e: Throwable) {
            false
        }

        assertThat(isLoggingInstalled).isTrue()
    }

    @Test
    fun testHttpClientFactory_timeoutIsInstalled() {
        val client = createMockClient()

        val isTimeoutInstalled = try {
            client.plugin(HttpTimeout)
            true
        } catch (e: Throwable) {
            false
        }

        assertThat(isTimeoutInstalled).isTrue()
    }

    @Test
    fun testHttpClientFactory_contentNegotiationIsInstalled() {
        val client = createMockClient()

        val isJsonInstalled = try {
            client.plugin(ContentNegotiation)
            true
        } catch (e: Throwable) {
            false
        }

        assertThat(isJsonInstalled).isTrue()
    }

    private fun createMockClient(): HttpClient {
        val engine = MockEngine {
            respond("{}", HttpStatusCode.OK, headersOf("Content-Type", "application/json"))
        }
        return HttpClientFactory.create(engine)
    }
}
