package com.cinephile.app.core.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.net.UnknownHostException

@Suppress("UNNECESSARY_SAFE_CALL")
class HumanizeTest {

    @Test
    fun `unknown host becomes friendly offline message`() {
        val msg = humanize(UnknownHostException(), "fallback")
        assertThat(msg).contains("internet")
    }

    @Test
    fun `unknown exceptions keep the fallback message`() {
        val msg = humanize(IllegalStateException("boom"), "fallback text")
        assertThat(msg).isEqualTo("fallback text")
    }
}
