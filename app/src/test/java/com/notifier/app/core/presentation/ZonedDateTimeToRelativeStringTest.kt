package com.notifier.app.core.presentation

import com.google.common.truth.Truth.assertThat
import com.notifier.app.core.presentation.util.toRelativeTimeString
import org.junit.Test
import java.time.ZonedDateTime

class ZonedDateTimeToRelativeStringTest {
    private val now: ZonedDateTime = ZonedDateTime.now()

    @Test
    fun testToRelativeTimeString_withSameTime_returns0s() {
        val time = now
        assertThat(time.toRelativeTimeString()).isEqualTo("0s")
    }

    @Test
    fun testToRelativeTimeString_with59SecondsAgo_returns59s() {
        val time = now.minusSeconds(59)
        assertThat(time.toRelativeTimeString()).isEqualTo("59s")
    }

    @Test
    fun testToRelativeTimeString_with60SecondsAgo_returns1m() {
        val time = now.minusSeconds(60)
        assertThat(time.toRelativeTimeString()).isEqualTo("1m")
    }

    @Test
    fun testToRelativeTimeString_with3599SecondsAgo_returns59m() {
        val time = now.minusSeconds(3599)
        assertThat(time.toRelativeTimeString()).isEqualTo("59m")
    }

    @Test
    fun testToRelativeTimeString_with3600SecondsAgo_returns1h() {
        val time = now.minusSeconds(3600)
        assertThat(time.toRelativeTimeString()).isEqualTo("1h")
    }

    @Test
    fun testToRelativeTimeString_with86399SecondsAgo_returns23h() {
        val time = now.minusSeconds(86399)
        assertThat(time.toRelativeTimeString()).isEqualTo("23h")
    }

    @Test
    fun testToRelativeTimeString_with86400SecondsAgo_returns1d() {
        val time = now.minusSeconds(86400)
        assertThat(time.toRelativeTimeString()).isEqualTo("1d")
    }

    @Test
    fun testToRelativeTimeString_with604799SecondsAgo_returns6d() {
        val time = now.minusSeconds(604799)
        assertThat(time.toRelativeTimeString()).isEqualTo("6d")
    }

    @Test
    fun testToRelativeTimeString_with604800SecondsAgo_returns1w() {
        val time = now.minusSeconds(604800)
        assertThat(time.toRelativeTimeString()).isEqualTo("1w")
    }

    @Test
    fun testToRelativeTimeString_withFutureTimeWithinAnHour_returns25m() {
        val time = now.plusMinutes(25)
        assertThat(time.toRelativeTimeString()).isEqualTo("25m")
    }

    @Test
    fun testToRelativeTimeString_with90DaysInFuture_returns12w() {
        val time = now.plusDays(90)
        assertThat(time.toRelativeTimeString()).isEqualTo("12w")
    }

    @Test
    fun testToRelativeTimeString_with365DaysInPast_returns52w() {
        val time = now.minusDays(365)
        assertThat(time.toRelativeTimeString()).isEqualTo("${365 / 7}w")
    }

    @Test
    fun testToRelativeTimeString_with119SecondsAgo_returns1m() {
        val time = now.minusSeconds(119)
        assertThat(time.toRelativeTimeString()).isEqualTo("1m")
    }

    @Test
    fun testToRelativeTimeString_withNegativeDuration_returns5h() {
        val time = now.plusHours(5)
        assertThat(time.toRelativeTimeString()).isEqualTo("5h")
    }

    @Test
    fun testToRelativeTimeString_with1000DaysInFuture_returns142w() {
        val time = now.plusDays(1000)
        assertThat(time.toRelativeTimeString()).isEqualTo("${1000 / 7}w")
    }
}
