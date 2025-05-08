package com.notifier.app.core.domain.util

enum class PersistenceError : Error {
    IO,
    SERIALIZATION,
    UNKNOWN,
}
