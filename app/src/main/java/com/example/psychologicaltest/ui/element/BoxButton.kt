package com.example.psychologicaltest.ui.element

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.psychologicaltest.ui.theme.Black0
import com.example.psychologicaltest.ui.theme.Gray40
import com.example.psychologicaltest.ui.theme.Orange50
import com.example.psychologicaltest.ui.theme.White0

@Composable
fun BoxButton(
    onClick: () -> Unit,
    execute: Boolean = false,
    isSelected: Boolean = false,
    text: String,
    modifier: Modifier,
    padding: PaddingValues = PaddingValues(8.dp)
) {
    Box(
        modifier = modifier
            .border(BorderStroke(2.dp, if(execute || isSelected) Orange50 else Gray40))
            .background(color = if(isSelected) Orange50 else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(padding)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text,
            color = if(isSelected) Black0 else White0,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}