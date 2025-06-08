package com.notifier.app.core.presentation.util

import java.time.Duration
import java.time.ZonedDateTime
import kotlin.math.abs

fun ZonedDateTime.toRelativeTimeString(): String {
    val now = ZonedDateTime.now()
    val duration = Duration.between(this, now)
    val seconds = abs(duration.seconds)

    return when {
        seconds < 60 -> "${seconds}s"
        seconds < 60 * 60 -> "${seconds / 60}m"
        seconds < 60 * 60 * 24 -> "${seconds / 3600}h"
        seconds < 60 * 60 * 24 * 7 -> "${seconds / 86400}d"
        else -> "${seconds / (86400 * 7)}w"
    }
}

