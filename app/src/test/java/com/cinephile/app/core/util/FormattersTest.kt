package com.cinephile.app.core.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class FormattersTest {

    @Test
    fun `posterUrl builds full url from path`() {
        assertThat(Formatters.posterUrl("/abc.jpg")).isEqualTo("https://image.tmdb.org/t/p/w342/abc.jpg")
    }

    @Test
    fun `posterUrl returns null for null and empty paths`() {
        assertThat(Formatters.posterUrl(null)).isNull()
        assertThat(Formatters.posterUrl("")).isNull()
    }

    @Test
    fun `yearOf extracts year from release date`() {
        assertThat(Formatters.yearOf("2019-07-26")).isEqualTo("2019")
        assertThat(Formatters.yearOf(null)).isEqualTo("")
        assertThat(Formatters.yearOf("abcd-ef-gh")).isEqualTo("")
    }

    @Test
    fun `formatRuntime formats hours and minutes`() {
        assertThat(Formatters.formatRuntime(134)).isEqualTo("2h 14m")
        assertThat(Formatters.formatRuntime(60)).isEqualTo("1h")
        assertThat(Formatters.formatRuntime(45)).isEqualTo("45m")
        assertThat(Formatters.formatRuntime(null)).isEqualTo("—")
        assertThat(Formatters.formatRuntime(0)).isEqualTo("—")
    }

    @Test
    fun `formatDate renders readable date`() {
        assertThat(Formatters.formatDate("2019-07-26")).isEqualTo("26 Jul 2019")
        assertThat(Formatters.formatDate(null)).isEqualTo("")
    }

    @Test
    fun `rating formats one decimal`() {
        assertThat(Formatters.rating(6.75)).isEqualTo("6.8")
        assertThat(Formatters.rating(null)).isEqualTo("—")
    }

    @Test
    fun `formatTotalWatchTime formats long durations`() {
        assertThat(Formatters.formatTotalWatchTime(4823)).isEqualTo("80h 23m")
    }
}
