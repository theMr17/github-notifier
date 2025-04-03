package com.notifier.app.core.data.networking

import com.notifier.app.BuildConfig

/**
 * Constructs a complete URL by ensuring it is prefixed with the base URL.
 *
 * This function checks whether the provided [url] already contains the base URL.
 * If not, it appends the base URL accordingly.
 *
 * @param url The relative or absolute URL to be processed.
 * @return The fully constructed URL with the appropriate base URL.
 */
fun constructUrl(url: String): String {
    return when {
        // If the URL already contains the base URL, return it as is
        url.contains(BuildConfig.BASE_URL) -> url

        // If the URL starts with "/", append it to the base URL after removing the leading slash
        url.startsWith("/") -> BuildConfig.BASE_URL + url.drop(1)

        // If the URL is a relative path without a leading slash, append it directly to the base URL
        else -> BuildConfig.BASE_URL + url
    }
}
