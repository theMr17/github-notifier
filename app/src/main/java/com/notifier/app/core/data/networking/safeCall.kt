package com.notifier.app.core.data.networking

import com.notifier.app.core.domain.util.NetworkError
import com.notifier.app.core.domain.util.Result
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import kotlin.coroutines.coroutineContext

/**
 * Executes a network call safely, handling exceptions and returning a [Result].
 *
 * This function wraps a network call inside a try-catch block to handle common errors such as
 * internet unavailability, serialization issues, and unknown errors. It ensures that the coroutine
 * remains active before returning a result.
 *
 * @param T The expected response type.
 * @param execute A lambda function that performs the network request and returns an [HttpResponse].
 * @return A [Result] containing either the successful response data or a [NetworkError].
 */
suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse,
): Result<T, NetworkError> {
    val response = try {
        execute()
    } catch (e: UnresolvedAddressException) {
        return Result.Error(NetworkError.NO_INTERNET)
    } catch (e: SerializationException) {
        return Result.Error(NetworkError.SERIALIZATION)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        return Result.Error(NetworkError.UNKNOWN)
    }

    return responseToResult(response)
}
