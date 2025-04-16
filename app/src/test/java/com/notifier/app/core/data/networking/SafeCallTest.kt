package com.notifier.app.core.data.networking

import com.google.common.truth.Truth.assertThat
import com.notifier.app.core.domain.util.NetworkError
import com.notifier.app.core.domain.util.Result
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.util.network.UnresolvedAddressException
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import org.junit.Test

class SafeCallTest {
    @Test
    fun testSafeCall_withSuccessfulResponse_returnsSuccessResult() = runTest {
        val mockResponse = mockk<HttpResponse> {
            every { status } returns HttpStatusCode.OK
            every { headers } returns headersOf(HttpHeaders.ContentType, "application/json")
            every { call } returns mockk()
            coEvery { body<String>() } returns "success"
        }

        val result = safeCall<String> { mockResponse }

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo("success")
    }

    @Test
    fun testSafeCall_withUnresolvedAddressException_returnsNoInternetError() = runTest {
        val result = safeCall<HttpResponse> { throw UnresolvedAddressException() }

        assertThat(result).isEqualTo(Result.Error(NetworkError.NO_INTERNET))
    }

    @Test
    fun testSafeCall_withSerializationException_returnsSerializationError() = runTest {
        val result = safeCall<HttpResponse> { throw SerializationException() }

        assertThat(result).isEqualTo(Result.Error(NetworkError.SERIALIZATION))
    }

    @Test
    fun testSafeCall_withUnknownException_returnsUnknownError() = runTest {
        val result = safeCall<HttpResponse> { throw Exception() }

        assertThat(result).isEqualTo(Result.Error(NetworkError.UNKNOWN))
    }
}
