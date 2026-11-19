package com.tttt.app

import org.junit.Assert.assertEquals
import org.junit.Test

class ClickMessageTest {
    @Test
    fun noClicks() {
        assertEquals("Tap the button", clickMessage(0))
    }

    @Test
    fun oneClick() {
        assertEquals("Tapped 1 time", clickMessage(1))
    }

    @Test
    fun manyClicks() {
        assertEquals("Tapped 5 times", clickMessage(5))
    }
}
