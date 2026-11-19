package com.tttt.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ButtonScreen()
            }
        }
    }
}

/** A screen with one button that counts how many times it was pressed. */
@Composable
fun ButtonScreen() {
    // rememberSaveable keeps the count when the screen rotates.
    var clicks by rememberSaveable { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = clickMessage(clicks), style = MaterialTheme.typography.headlineSmall)
        Button(onClick = { clicks++ }) {
            Text("Press me")
        }
    }
}

/** The text shown above the button. Plain function, so it is easy to unit-test. */
fun clickMessage(clicks: Int): String = when (clicks) {
    0 -> "Tap the button"
    1 -> "Tapped 1 time"
    else -> "Tapped $clicks times"
}
