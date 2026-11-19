package com.tttt.app.ui.counter

import org.junit.Assert.assertEquals
import org.junit.Test

class CounterViewModelTest {
    @Test
    fun startsAtZero() {
        val viewModel = CounterViewModel()
        assertEquals(0, viewModel.uiState.value.clicks)
    }

    @Test
    fun clickIncrementsCounter() {
        val viewModel = CounterViewModel()
        viewModel.onButtonClick()
        viewModel.onButtonClick()
        assertEquals(2, viewModel.uiState.value.clicks)
    }
}
