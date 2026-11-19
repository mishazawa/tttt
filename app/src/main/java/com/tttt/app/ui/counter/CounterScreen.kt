package com.tttt.app.ui.counter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tttt.app.R
import com.tttt.app.ui.theme.TtttTheme

/** Connects the [CounterViewModel] to the screen. */
@Composable
fun CounterRoute(viewModel: CounterViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CounterScreen(uiState = uiState, onButtonClick = viewModel::onButtonClick)
}

/** Stateless UI: draws [uiState] and reports clicks. Easy to preview. */
@Composable
fun CounterScreen(
    uiState: CounterUiState,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val message = if (uiState.clicks == 0) {
                stringResource(R.string.tap_the_button)
            } else {
                pluralStringResource(R.plurals.tapped_times, uiState.clicks, uiState.clicks)
            }
            Text(text = message, style = MaterialTheme.typography.headlineSmall)
            Button(onClick = onButtonClick) {
                Text(stringResource(R.string.press_me))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CounterScreenPreview() {
    TtttTheme {
        CounterScreen(uiState = CounterUiState(clicks = 3), onButtonClick = {})
    }
}
