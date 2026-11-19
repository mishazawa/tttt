package com.tttt.app.ui.counter

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** Everything the counter screen needs to draw itself. */
data class CounterUiState(val clicks: Int = 0)

/** Holds the counter state so it survives screen rotation. */
class CounterViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CounterUiState())
    val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()

    fun onButtonClick() {
        _uiState.update { it.copy(clicks = it.clicks + 1) }
    }
}
