package com.example.psychologicaltest.ui.theme

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.FloatExponentialDecaySpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntOffset
import androidx.wear.compose.material.swipeable
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
suspend fun callState(newValue: Int, state: LazyListState) {
    state.animateScrollToItem(newValue)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PsyhoTestScreen(
    modifier: Modifier = Modifier,
    title: String,
    questions: List<String>,
    @StringRes options: List<Int>,
    onSendAppButton: (subject: String, summary: String) -> Unit = { s: String, s1: String -> },
    onCancelButton: () -> Unit = {}
) {
    val context = LocalContext.current
//    var selectedValues: Array<Int?> by rememberSaveable { mutableStateOf(arrayOfNulls(questions.size)) }
//    var currentQuestion: Int by rememberSaveable { mutableStateOf(0) }
    val squareSize = 48.dp
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val density = LocalDensity.current

    val lazyState: LazyListState = rememberLazyListState()

    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val coroutineScope = rememberCoroutineScope()
    val state = remember {
        AnchoredDraggableState(
            initialValue = 0,
            anchors = DraggableAnchors {
                repeat(questions.size) { index ->
                    index at (screenWidth * index).value
                }
            },
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            snapAnimationSpec = tween(),
            decayAnimationSpec = decayAnimationSpec,
            confirmValueChange = { newValue: Int ->
                Log.d("newValue", newValue.toString())
                coroutineScope.launch {
                    callState(newValue = newValue, lazyState)
                }
                true
            }
        )
    }

    LazyRow(userScrollEnabled = false, state = lazyState, modifier = Modifier.wrapContentSize()) {
        items(questions) { question ->
            Box(
                modifier = Modifier
                    .width(screenWidth)
                    .height(screenHeight)
                    .background(Color.Yellow)
                    .padding(20.dp)
                    .anchoredDraggable(state, orientation = Orientation.Horizontal, reverseDirection = true)
            ) {
                Column(
                    modifier = Modifier
                ) {
                    Text(text = title)
                    Text(text = question)
                    Column {
                        options.forEach { id ->
                            Text(text = context.getString(id).lowercase())
                        }
                    }
                }
            }
        }
    }
}

