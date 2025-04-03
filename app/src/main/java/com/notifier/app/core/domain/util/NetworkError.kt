package com.notifier.app.core.domain.util

/**
 * Enum representing different types of network-related errors.
 */
enum class NetworkError : Error {
    /** The request timed out before receiving a response. */
    REQUEST_TIMEOUT,

    /** Too many requests were sent in a short period (rate limiting). */
    TOO_MANY_REQUESTS,

    /** No internet connection is available. */
    NO_INTERNET,

    /** A server error occurred (HTTP 5xx status codes). */
    SERVER_ERROR,

    /** Serialization error occurred while parsing the response. */
    SERIALIZATION,

    /** An unknown error occurred. */
    UNKNOWN,
}
