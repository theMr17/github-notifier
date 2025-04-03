package com.notifier.app.core.domain.util

/**
 * A type alias representing an error type in the domain layer.
 *
 * This alias is used to standardize error handling throughout the domain logic.
 */
typealias DomainError = Error

/**
 * A sealed interface representing the result of an operation that can either be successful
 * or return an error.
 *
 * @param D The type of successful data.
 * @param E The type of error, which must extend [Error].
 */
sealed interface Result<out D, out E : Error> {
    /**
     * Represents a successful operation result containing the expected data.
     *
     * @param data The successfully retrieved data.
     */
    data class Success<out D>(val data: D) : Result<D, Nothing>

    /**
     * Represents an error result containing an error of type [E].
     *
     * @param error The error that occurred.
     */
    data class Error<out E : DomainError>(val error: E) : Result<Nothing, E>
}

/**
 * Transforms the successful data inside a [Result] using the given mapping function.
 *
 * @param map A function to transform the success data.
 * @return A new [Result] with the transformed data if successful; otherwise, returns the original error.
 */
inline fun <T, E : Error, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when (this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(data))
    }
}

/**
 * Converts a [Result] into an [EmptyResult], which discards the success data but retains the error.
 *
 * @return A [Result] with `Unit` as its success type.
 */
fun <T, E : Error> Result<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map { }
}

/**
 * Executes the given action if the [Result] is successful.
 *
 * @param action A function to execute if the result is [Result.Success].
 * @return The original [Result] to allow method chaining.
 */
inline fun <T, E : Error> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> this
        is Result.Success -> {
            action(data)
            this
        }
    }
}

/**
 * Executes the given action if the [Result] contains an error.
 *
 * @param action A function to execute if the result is [Result.Error].
 * @return The original [Result] to allow method chaining.
 */
inline fun <T, E : Error> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> {
            action(error)
            this
        }
        is Result.Success -> this
    }
}

/**
 * A type alias for a [Result] that contains no success data but may contain an error.
 */
typealias EmptyResult<E> = Result<Unit, E>
