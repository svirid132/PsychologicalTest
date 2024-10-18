package com.example.psychologicaltest.ui.theme

import android.app.AlertDialog
import androidx.annotation.StringRes
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalDensity
import com.example.psychologicaltest.ui.element.BoxButton
import com.example.psychologicaltest.ui.element.SelectedButton
import kotlinx.coroutines.delay

suspend fun scrollLiner(lazyState: LazyListState, index: Int, screenWidthPx: Float) {
    val itemInfo: LazyListItemInfo? =
        lazyState.layoutInfo.visibleItemsInfo.find { it.index == index }
    // проверяю влезает ли кнопка справа или слева от краёв телефона
    val btnWidth = itemInfo?.size ?: 0
    val rightVisibleWidth = screenWidthPx - (itemInfo?.offset
        ?: 0) - btnWidth // Если кнопка справа
    val leftVisibleWidth =
        (itemInfo?.offset?.toFloat() ?: 0f) // Если кнопка слева
    if (itemInfo == null) {
        val count =
            (lazyState.layoutInfo.visibleItemsInfo.last().index - lazyState.layoutInfo.visibleItemsInfo.first().index) / 2
        val scrollCount = if (count >= 0) count else 0
        val tmpCurrentIndex = index - scrollCount
        val currentIndex = if (tmpCurrentIndex >= 0) tmpCurrentIndex else 0
        lazyState.scrollToItem(currentIndex)
    }
    if (rightVisibleWidth < 0) {
        lazyState.animateScrollBy(-rightVisibleWidth)
    }
    if (leftVisibleWidth < 0) {
        lazyState.animateScrollBy(leftVisibleWidth)
    }
}

suspend fun scrollPage(pagerState: PagerState, index: Int) {
    pagerState.animateScrollToPage(index)
}

@Composable
fun PsyhoTestScreen(
    modifier: Modifier = Modifier,
    title: String,
    questions: List<String>,
    @StringRes options: List<Int>,
    @StringRes answers: List<Int?>,
    onSelectAnswer: (answer: Int, index: Int) -> Unit = { i: Int, i1: Int -> }
) {
    val context = LocalContext.current

    val pagerState = rememberPagerState(
        initialPage = answers.indexOfFirst { it == null }.let {
            if (it == -1) 0 else it
        },
        pageCount = {
            questions.size
        })
    val lazyState = rememberLazyListState()
    val buttonWidth = 50.dp
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenWidthPx = with(LocalDensity.current) { screenWidth.toPx() }
    val coroutineScope = rememberCoroutineScope()

    // если был swipe
    LaunchedEffect(pagerState.currentPage) {
        coroutineScope.launch {
            scrollLiner(
                lazyState = lazyState,
                index = pagerState.currentPage,
                screenWidthPx = screenWidthPx
            )
        }
    }

    // диалоговое окно reset
    LaunchedEffect(Unit) {
        val testStarted = answers.fold(false) { res, value -> res || (value != null) }
        if (testStarted) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder
                .setMessage("Тест не закончен")
                .setTitle("Пожалуйста, дозаполните тест или сбросьте его, чтобы пройти его заново.")
                .setPositiveButton("Продолжить") { dialog, which -> }
                .setNegativeButton("Сбросить") { dialog, which ->
                    coroutineScope.launch {
                        delay(200L)
                        pagerState.scrollToPage(0)
                    }
                    coroutineScope.launch {
                        delay(200L)
                        scrollLiner(
                            lazyState = lazyState,
                            index = 0,
                            screenWidthPx = screenWidthPx
                        )
                    }
                }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    Box(modifier = modifier) {
        Column {
            Box(modifier = Modifier.padding(10.dp)) {
                Text(text = title, style = Typography.titleLarge)
            }
            LazyRow(state = lazyState, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(questions.size) { index ->
                    BoxButton(
                        isSelected = pagerState.currentPage == index,
                        modifier = Modifier.width(buttonWidth),
                        text = "${index + 1}",
                        execute = answers[index] != null,
                        onClick = {
                            coroutineScope.launch {
                                scrollLiner(
                                    lazyState = lazyState,
                                    index = index,
                                    screenWidthPx = screenWidthPx
                                )
                            }
                            coroutineScope.launch {
                                pagerState.scrollToPage(index)
                            }
                        })
                }
            }
            HorizontalPager(state = pagerState, beyondViewportPageCount = 3) { page ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    Column(
                        modifier = Modifier
                    ) {
                        Text(text = questions[page], style = Typography.bodyLarge)
                        Spacer(modifier = Modifier.weight(1f))
                        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            options.forEach { id ->
                                SelectedButton(
                                    onClick = {
                                        val currentPage = pagerState.currentPage
                                        val nextCurrentPage = currentPage + 1
                                        val preIndex =
                                            answers.subList(nextCurrentPage, answers.size)
                                                .indexOf(null)
                                        val index =
                                            if (preIndex == -1) answers.indexOf(null) else preIndex + nextCurrentPage
                                        coroutineScope.launch {
                                            delay(200L)
                                            scrollLiner(
                                                lazyState = lazyState,
                                                index = index,
                                                screenWidthPx = screenWidthPx
                                            )
                                        }
                                        coroutineScope.launch {
                                            delay(200L)
                                            pagerState.scrollToPage(index)
                                        }
                                        onSelectAnswer(id, page)
                                    },
                                    text = context.getString(id).lowercase(),
                                    isSelected = answers[page] == id,
                                    modifier = Modifier
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

