package com.notifier.app.core.data.networking

import com.google.common.truth.Truth.assertThat
import com.notifier.app.BuildConfig
import org.junit.Test

class ConstructUrlTest {
    @Test
    fun testConstructUrl_withAbsoluteUrl_returnsSameUrl() {
        val url = "${BuildConfig.BASE_URL}/data"
        val result = constructUrl(url)

        assertThat(result).isEqualTo(url)
    }

    @Test
    fun testConstructUrl_withRelativeUrlWithoutLeadingSlash_appendsToBaseUrl() {
        val url = "data"
        val result = constructUrl(url)

        assertThat(result).isEqualTo(BuildConfig.BASE_URL + url)
    }

    @Test
    fun testConstructUrl_withRelativeUrlWithLeadingSlash_appendsToBaseUrl() {
        val url = "/data"
        val result = constructUrl(url)

        assertThat(result).isEqualTo(BuildConfig.BASE_URL + "data")
    }

    @Test
    fun testConstructUrl_withEmptyUrl_returnsBaseUrl() {
        val url = ""
        val result = constructUrl(url)

        assertThat(result).isEqualTo(BuildConfig.BASE_URL)
    }
}
