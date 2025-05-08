package com.notifier.app.core.data.persistence

import com.notifier.app.core.domain.util.PersistenceError
import com.notifier.app.core.domain.util.Result
import kotlinx.coroutines.ensureActive
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.coroutineContext

/**
 * Executes a suspend block safely, returning a [Result] that wraps either the success value
 * or a [PersistenceError] in case of exception.
 *
 * Use this to wrap DataStore operations that might fail (e.g., IO, serialization).
 *
 * @param block the suspend lambda to execute.
 * @return a [Result.Success] if the block executes without exception,
 * or a [Result.Error] with appropriate [PersistenceError].
 */
suspend inline fun <T> runDataStoreCatching(
    crossinline block: suspend () -> T,
): Result<T, PersistenceError> {
    return try {
        val result = block()
        Result.Success(result)
    } catch (e: CancellationException) {
        throw e // Let coroutine cancellation propagate
    } catch (e: IOException) {
        Result.Error(PersistenceError.IO)
    } catch (e: SerializationException) {
        Result.Error(PersistenceError.SERIALIZATION)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        Result.Error(PersistenceError.UNKNOWN)
    }
}
