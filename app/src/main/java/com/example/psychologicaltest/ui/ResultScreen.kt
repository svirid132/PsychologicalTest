package com.example.psychologicaltest.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.psychologicaltest.StartScreen
import com.example.psychologicaltest.ui.theme.PsychologicalTestTheme
import com.example.psychologicaltest.ui.theme.Typography

@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    onReset: () -> Unit
) {
    Box(modifier = modifier) {
        Column {
            Box(modifier = Modifier.padding(10.dp)) {
                Text(text = title, style = Typography.titleLarge)
            }
            Box(modifier = Modifier.weight(1f)) {
                Text(text = description, style = Typography.bodyMedium)
            }
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Button(onClick = { onReset() }) {
                    Text(text = "Сбросить результат")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ResultScreenPreview() {
    PsychologicalTestTheme {
        ResultScreen(title = "title", description = "Descriptio, Descriptio!!!", onReset = {})
    }
}