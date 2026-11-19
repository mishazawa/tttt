package com.tttt.app

import org.junit.Assert.assertEquals
import org.junit.Test

class CounterTest {
    @Test
    fun startsAtZero() {
        assertEquals("Tap the button", Counter().label)
    }

    @Test
    fun clickIncrementsAndPluralizes() {
        val once = Counter().click()
        assertEquals("Tapped 1 time", once.label)
        assertEquals("Tapped 2 times", once.click().label)
    }
}
