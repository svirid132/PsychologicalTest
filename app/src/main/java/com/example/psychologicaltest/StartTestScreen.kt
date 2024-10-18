package com.example.psychologicaltest

import android.app.AlertDialog
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.psychologicaltest.data.PsychoTestToObject
import com.example.psychologicaltest.data.psycho_tests.PsychoTests
import com.example.psychologicaltest.ui.PsychoTestViewModel
import com.example.psychologicaltest.ui.ResultScreen
import com.example.psychologicaltest.ui.theme.PsyhoTestScreen
import com.example.psychologicaltest.ui.theme.scrollLiner
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

enum class AppScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name), AbilityToEmpathize(title = R.string.abilities_to_empatnice_name), ResultTest(
        title = R.string.result_screen
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestPsyhoAppBar(
    currentScreen: AppScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    titleArg: String
) {
    TopAppBar(title = { Text(stringResource(currentScreen.title, titleArg)) },
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
fun PsyhoTestApp(
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
    val titleArg = psychoTest?.testData?.name?.let { context.resources.getString(it) }
    Scaffold(
        topBar = {
            TestPsyhoAppBar(
                canNavigateBack = navController.previousBackStackEntry != null,
                currentScreen = currentScreen,
                navigateUp = {
                    if (currentScreen == AppScreen.ResultTest) {
                        navController.navigate(AppScreen.Start.name) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    }
                    navController.navigateUp()
                },
                titleArg = titleArg ?: ""
            )
        }, modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController, startDestination = AppScreen.Start.name, Modifier.padding(innerPadding)
        ) {
            composable(route = AppScreen.Start.name) {
                val coroutineScope = rememberCoroutineScope()
                StartScreen(
                    onNextButton = { psychoTest ->
                        psychoTestViewModel.selectPsychoTest(psychoTest)
                        coroutineScope.launch {
                            psychoTestViewModel.uiState.collect { state ->
                                if (state == null) {
                                    return@collect
                                }
                                val answerOrNull = state.answers
                                if (answerOrNull == null) {
                                    navController.navigate(AppScreen.AbilityToEmpathize.name)
                                } else {
                                    val ind = answerOrNull.indexOf(null)
                                    if (ind == -1) {
                                        navController.navigate(AppScreen.ResultTest.name)
                                    } else {
                                        navController.navigate(AppScreen.AbilityToEmpathize.name)
                                    }
                                }
                            }
                        }
                    }, modifier = Modifier.fillMaxSize()
                )
            }
            // Психологические тесты
            composable(route = AppScreen.AbilityToEmpathize.name) {
                val psychoTest by psychoTestViewModel.uiState.collectAsState()
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


                    PsyhoTestScreen(
                        title = stringResource(id = AppScreen.AbilityToEmpathize.title),
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
                val psychoTest by psychoTestViewModel.uiState.collectAsState()
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
                                    navController.navigate(AppScreen.AbilityToEmpathize.name)
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
    onNextButton: (psychoTest: PsychoTests) -> Unit = { psychoTests: PsychoTests -> }
) {
    Box(modifier = modifier.fillMaxSize()) {
        val testToObjList = PsychoTestToObject.getList()
        val context = LocalContext.current
        var selectedTestByInd by remember {
            mutableStateOf<Int?>(null)
        }
        Row {
            val testNameList = testToObjList.map { context.resources.getString(it.second.name) }
            TestNameList(
                nameList = testNameList,
                onClick = { ind -> selectedTestByInd = ind },
                modifier = Modifier.weight(0.5f)
            )
            if (selectedTestByInd == null) {
                Box(modifier = Modifier.weight(0.5f))
            } else {
                val name =
                    context.resources.getString(testToObjList[selectedTestByInd!!].second.name)
                val description =
                    context.resources.getString(testToObjList[selectedTestByInd!!].second.description)
                InfoTest(name = name, description = description, modifier = Modifier.weight(0.5f))
            }
        }
        Button(
            onClick = {
                val test = testToObjList[selectedTestByInd!!].first
                onNextButton(test)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(0.dp, 0.dp, 20.dp, 20.dp),
            enabled = selectedTestByInd != null
        ) {
            Text(text = "Запуск")
        }
    }
}

@Composable
fun TestNameList(nameList: List<String>, onClick: (Int) -> Unit, modifier: Modifier) {
    Box(modifier = modifier) {
        Column(modifier = modifier.fillMaxSize()) {
            Text(
                text = "Тесты:", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
            )
            Column(
                Modifier.padding(10.dp, 0.dp, 0.dp, 0.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                nameList.forEachIndexed { ind, name ->
                    Button(onClick = { onClick(ind) }) {
                        Text(
                            text = name, textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoTest(name: String, description: String, modifier: Modifier) {
    Box(modifier = modifier) {
        Column {
            Text(
                text = name, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
            )
            Text(
                text = description, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PsyhoTestScreenPreview() {
    PsychologicalTestTheme {
        StartScreen()
    }
}