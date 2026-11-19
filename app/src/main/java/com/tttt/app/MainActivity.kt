package com.tttt.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tttt.app.ui.counter.CounterRoute
import com.tttt.app.ui.theme.TtttTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TtttTheme {
                CounterRoute()
            }
        }
    }
}
