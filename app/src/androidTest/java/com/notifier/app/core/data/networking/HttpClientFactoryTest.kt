package com.notifier.app.core.data.networking

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.api.ClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HttpClientFactoryTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var httpClientFactory: HttpClientFactory

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun testHttpClientFactory_createsHttpClientSuccessfully() {
        val client = createMockClient()

        assertThat(client).isInstanceOf(HttpClient::class.java)
    }

    @Test
    fun testHttpClientFactory_loggingIsInstalled() {
        assertThat(isPluginInstalled(Logging)).isTrue()
    }

    @Test
    fun testHttpClientFactory_timeoutIsInstalled() {
        assertThat(isPluginInstalled(HttpTimeout)).isTrue()
    }

    @Test
    fun testHttpClientFactory_contentNegotiationIsInstalled() {
        assertThat(isPluginInstalled(ContentNegotiation)).isTrue()
    }

    private fun createMockClient(): HttpClient {
        val engine = MockEngine {
            respond(
                content = "{}",
                status = HttpStatusCode.OK,
                headers = headersOf("Content-Type", "application/json")
            )
        }
        return httpClientFactory.create(engine)
    }

    private fun <T : Any> isPluginInstalled(plugin: ClientPlugin<T>): Boolean {
        val client = createMockClient()

        // Check if the plugin is installed by trying to access it
        return try {
            client.plugin(plugin)
            true
        } catch (e: Throwable) {
            println("Plugin ${plugin.key} not installed: ${e.message}")
            false
        }
    }
}
