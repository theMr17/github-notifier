package com.notifier.app.core.data.networking

import com.notifier.app.core.domain.util.NetworkError
import com.notifier.app.core.domain.util.Result
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

/**
 * Converts an [HttpResponse] to a [Result] object, handling different HTTP status codes
 * and potential serialization errors.
 *
 * @param T The expected response type.
 * @param response The [HttpResponse] received from the network request.
 * @return A [Result] containing either the parsed response data or a [NetworkError].
 */
suspend inline fun <reified T> responseToResult(
    response: HttpResponse,
): Result<T, NetworkError> {
    return when (response.status.value) {
        in 200..299 -> {
            try {
                Result.Success(response.body<T>())
            } catch (e: NoTransformationFoundException) {
                e.printStackTrace()
                Result.Error(NetworkError.SERIALIZATION)
            }
        }

        408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
        429 -> Result.Error(NetworkError.TOO_MANY_REQUESTS)
        in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
        else -> Result.Error(NetworkError.UNKNOWN)
    }
}
