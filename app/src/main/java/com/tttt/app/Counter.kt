package com.tttt.app

/** Pure UI state for the button screen; kept free of Android types so it is unit-testable. */
data class Counter(val clicks: Int = 0) {
    fun click(): Counter = copy(clicks = clicks + 1)

    val label: String
        get() = if (clicks == 0) "Tap the button" else "Tapped $clicks time${if (clicks == 1) "" else "s"}"
}
