package com.notifier.app.core.presentation.util

import java.time.Duration
import java.time.ZonedDateTime
import kotlin.math.abs

fun ZonedDateTime.toRelativeTimeString(): String {
    val now = ZonedDateTime.now()
    val duration = Duration.between(this, now)
    val seconds = duration.seconds

    val absSeconds = abs(seconds)

    val minute = 60
    val hour = 60 * minute
    val day = 24 * hour
    val week = 7 * day

    return when {
        absSeconds < minute -> "${absSeconds}s"
        absSeconds < hour -> "${absSeconds / minute}m"
        absSeconds < day -> "${absSeconds / hour}h"
        absSeconds < week -> "${absSeconds / day}d"
        else -> "${absSeconds / week}w"
    }
}
