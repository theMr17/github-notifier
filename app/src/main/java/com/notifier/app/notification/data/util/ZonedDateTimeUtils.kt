package com.notifier.app.notification.data.util

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Extension function to safely parse a [String] into a [ZonedDateTime].
 *
 * This function attempts to parse the string as an [Instant] and then convert it
 * to the system's default time zone. If parsing fails due to an invalid format
 * or other exceptions, a default value is returned instead.
 *
 * @receiver The string representation of an [Instant] (e.g., "2023-10-15T12:34:56Z").
 * @param default The fallback [ZonedDateTime] value to return if parsing fails.
 *                Defaults to [Instant.EPOCH] at the system's default time zone.
 * @return The parsed [ZonedDateTime] or the provided default value if parsing fails.
 */
fun String?.toZonedDateTimeOrDefault(
    default: ZonedDateTime = Instant.EPOCH.atZone(ZoneId.systemDefault()),
): ZonedDateTime {
    return try {
        Instant.parse(this).atZone(ZoneId.systemDefault())
    } catch (e: Exception) {
        e.printStackTrace()
        default
    }
}
