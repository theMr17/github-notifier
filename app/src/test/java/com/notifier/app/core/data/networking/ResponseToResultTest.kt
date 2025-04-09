package com.notifier.app.core.data.networking

import com.google.common.truth.Truth.assertThat
import com.notifier.app.core.domain.util.NetworkError
import com.notifier.app.core.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.Test

class ResponseToResultTest {
    private val mockBaseUrl = "https://test.com"

    private val mockEngine = MockEngine { request ->
        when (request.url.encodedPath) {
            "/success" -> respond(
                content = """{"data":"success"}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )

            "/timeout" -> respond(content = "", status = HttpStatusCode.RequestTimeout)
            "/too_many_requests" -> respond(content = "", status = HttpStatusCode.TooManyRequests)
            "/server_error" -> respond(content = "", status = HttpStatusCode.InternalServerError)
            else -> respond(content = "", status = HttpStatusCode.BadRequest)
        }
    }

    private val client = HttpClient(mockEngine) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    @Test
    fun testResponseToResult_with2xxResponse_returnsSuccess() = runBlocking {
        @Serializable
        data class SuccessResponse(val data: String)

        val response = client.get("${mockBaseUrl}/success")
        val result: Result<SuccessResponse, NetworkError> = responseToResult(response)

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data.data).isEqualTo("success")
    }

    @Test
    fun testResponseToResult_with408Response_returnsRequestTimeout() = runBlocking {
        val response = client.get("${mockBaseUrl}/timeout")
        val result: Result<Unit, NetworkError> = responseToResult(response)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(NetworkError.REQUEST_TIMEOUT)
    }

    @Test
    fun testResponseToResult_with429Response_returnsTooManyRequests() = runBlocking {
        val response = client.get("${mockBaseUrl}/too_many_requests")
        val result: Result<Unit, NetworkError> = responseToResult(response)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(NetworkError.TOO_MANY_REQUESTS)
    }

    @Test
    fun testResponseToResult_with5xxResponse_returnsServerError() = runBlocking {
        val response = client.get("${mockBaseUrl}/server_error")
        val result: Result<Unit, NetworkError> = responseToResult(response)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(NetworkError.SERVER_ERROR)
    }

    @Test
    fun testResponseToResult_withUnknownResponse_returnsUnknownError() = runBlocking {
        val response = client.get("${mockBaseUrl}/unknown")
        val result: Result<Unit, NetworkError> = responseToResult(response)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(NetworkError.UNKNOWN)
    }
}
