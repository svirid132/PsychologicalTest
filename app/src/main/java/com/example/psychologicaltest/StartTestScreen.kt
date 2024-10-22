package com.example.psychologicaltest

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.psychologicaltest.ui.theme.PsychologicalTestTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign.Companion.Justify
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.psychologicaltest.data.PsychoTestToObject
import com.example.psychologicaltest.data.psycho_tests.PsychoTests
import com.example.psychologicaltest.ui.PsychoTestViewModel
import com.example.psychologicaltest.ui.ResultScreen
import com.example.psychologicaltest.ui.theme.PsyhoTestScreen
import com.example.psychologicaltest.ui.theme.Typography
import kotlinx.coroutines.launch

enum class AppScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Test(title = R.string.test_name),
    ResultTest(
        title = R.string.result_screen
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestPsychoAppBar(
    title: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(title = { Text(title) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_back),
                        contentDescription = stringResource(R.string.back_button),
                    )
                }
            }
        })
}

@Composable
fun PsychoTestApp(
    psychoTestViewModel: PsychoTestViewModel = viewModel(
        factory = PsychoTestViewModel.Factory
    ), navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.Start.name
    )
    val context = LocalContext.current
    val psychoTest by psychoTestViewModel.uiState.collectAsState()
    val title = currentScreen.title.let {
        val titleArg = psychoTest?.testData?.name?.let { context.resources.getString(it) }
        stringResource(currentScreen.title, titleArg ?: "")
    }
    val dialogTitle = psychoTest?.testData?.let {
        Log.d("this is", stringResource(it.name))
        stringResource(it.name)
    } ?: ""
    val dialogDescription = psychoTest?.testData?.let {
        stringResource(it.description)
    } ?: ""
    var showDialog by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            TestPsychoAppBar(
                title = title,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = {
                    if (currentScreen == AppScreen.ResultTest) {
                        navController.navigate(AppScreen.Start.name) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    }
                    navController.navigateUp()
                }
            )
        }, modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController, startDestination = AppScreen.Start.name, Modifier.padding(innerPadding),
            enterTransition = {
                return@NavHost when (currentScreen) {
                    AppScreen.Start -> fadeIn(animationSpec = tween(700))
                    else -> {
                        EnterTransition.None
                    }
                }
            },
            exitTransition = {
                return@NavHost when (currentScreen) {
                    AppScreen.Start -> fadeOut(animationSpec = tween(700))
                    else -> {
                        ExitTransition.None
                    }
                }
            },
        ) {
            composable(route = AppScreen.Start.name) {
                val coroutineScope = rememberCoroutineScope()
                StartScreen(
                    onSelectButton = { psychoTest ->
                        psychoTestViewModel.selectPsychoTest(psychoTest)
                        showDialog = true
                    }, modifier = Modifier.fillMaxSize()
                )
                if (showDialog) {
                    DescriptionDialog(
                        title = dialogTitle,
                        info = dialogDescription,
                        onStart = {
                            coroutineScope.launch {
                                psychoTestViewModel.uiState.collect { state ->
                                    if (state == null) {
                                        return@collect
                                    }
                                    val answerOrNull = state.answers
                                    if (answerOrNull == null) {
                                        navController.navigate(AppScreen.Test.name)
                                    } else {
                                        val ind = answerOrNull.indexOf(null)
                                        if (ind == -1) {
                                            navController.navigate(AppScreen.ResultTest.name)
                                        } else {
                                            navController.navigate(AppScreen.Test.name)
                                        }
                                    }
                                }
                            }
                            showDialog = false
                        },
                        onDismissRequest = {
                            showDialog = false
                        })
                }
            }
            // Психологические тесты
            composable(route = AppScreen.Test.name) {
                if (psychoTest == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.width(64.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    }
                } else {
                    val questions = psychoTest!!.testData.questions.map { id ->
                        context.resources.getString(
                            id
                        )
                    }
                    val options = psychoTest!!.testData.options
                    val answersOrNull = psychoTest!!.answers
                    val answers = answersOrNull ?: questions.map { null }
                    val titleArg = stringResource(psychoTest!!.testData.name)

                    PsyhoTestScreen(
                        title = stringResource(id = AppScreen.Test.title, titleArg),
                        questions = questions,
                        answers = answers,
                        onSelectAnswer = { newAnswer, index ->
                            val newAnswers =
                                answers.mapIndexed { ind, answer -> if (ind == index) newAnswer else answer }
                            psychoTestViewModel.saveTestAnswer(
                                PsychoTests.AbilityToEmpathize, newAnswers, onSaved = {
                                    val isCompleted = (newAnswers.indexOf(null) == -1)
                                    if (isCompleted) {
                                        navController.navigate(AppScreen.ResultTest.name)
                                    }
                                }
                            )
                        },
                        options = options.map { id -> id },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            composable(route = AppScreen.ResultTest.name) {
                val answers = psychoTest?.answers
                val result = answers?.let { psychoTest?.testData?.result(answers.filterNotNull()) }
                if (result != null) {
                    ResultScreen(
                        title = result.resultName, description = result.description, onReset = {
                            psychoTest?.let {
                                psychoTestViewModel.saveTestAnswer(it.currentTest, null, onSaved = {
                                    navController.navigate(AppScreen.Start.name) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            inclusive = true
                                        }
                                    }
                                    navController.navigate(AppScreen.Test.name)
                                })
                            }
                        }, modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    onSelectButton: (psychoTest: PsychoTests) -> Unit = { psychoTests: PsychoTests -> }
) {
    Box(modifier = modifier.fillMaxSize()) {
        val testToObjList = PsychoTestToObject.getList()
        val context = LocalContext.current
        val testNameList = testToObjList.map { context.resources.getString(it.second.name) }
        TestNameList(
            nameList = testNameList,
            onClick = { ind ->
                val test = testToObjList[ind].first
                onSelectButton(test)
            },
            modifier = Modifier
        )
    }
}

@Composable
fun TestNameList(nameList: List<String>, onClick: (Int) -> Unit, modifier: Modifier) {
    Box(modifier = modifier.verticalScroll(rememberScrollState())) {
        Column(
            Modifier
                .padding(10.dp, 10.dp, 10.dp, 0.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            nameList.forEachIndexed { ind, name ->
                Button(modifier = Modifier.fillMaxWidth(), onClick = { onClick(ind) }) {
                    Text(
                        text = name, textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun DescriptionDialog(
    title: String,
    info: String,
    onStart: () -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        ContentCard(title, info, onStart, onDismissRequest)
    }
}

@Composable
fun ContentCard(title: String, info: String, onStart: () -> Unit, onDismissRequest: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize()
                    .padding(16.dp)
            ) {
                Text(
                    style = Typography.titleLarge,
                    text = title,
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center,
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp, 16.dp, 8.dp)
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "\t $info",
                    textAlign = Justify,
                )
            }
            Row(
                modifier = Modifier
                    .padding(0.dp, 0.dp, 20.dp, 20.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onDismissRequest
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
                Button(
                    onClick = onStart
                ) {
                    Text(text = stringResource(id = R.string.run))
                }
            }
        }
    }
}

@Preview
@Composable
fun ContentCardPreview() {
    val title = "This is a minimal dialog This is a minimal dialog"
    var info = ""
    repeat(40) {
        info += "This is a minimal dialog This is a minimal dialog "
    }
    PsychologicalTestTheme {
        ContentCard(title, info, {}, {})
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PsychoTestScreenPreview() {
    PsychologicalTestTheme {
        StartScreen()
    }
}