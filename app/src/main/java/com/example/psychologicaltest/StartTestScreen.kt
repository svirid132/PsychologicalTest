package com.example.psychologicaltest

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.example.psychologicaltest.data.PsychoTests
import com.example.psychologicaltest.ui.PsychoTestViewModel

enum class PsyhoTestScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    AbilityToEmpathize(title = R.string.abilities_to_empatnice_name)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestPsyhoAppBar(
    currentScreen: PsyhoTestScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
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
        }
    )
}

@Composable
fun PsyhoTestApp(
    psychoTestViewModel: PsychoTestViewModel = viewModel(
        factory = PsychoTestViewModel.Factory
    ),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = PsyhoTestScreen.valueOf(
        backStackEntry?.destination?.route ?: PsyhoTestScreen.Start.name
    )
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TestPsyhoAppBar(
                canNavigateBack = navController.previousBackStackEntry != null,
                currentScreen = currentScreen,
                navigateUp = { navController.navigateUp() }
            )
        }, modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = PsyhoTestScreen.Start.name,
            Modifier.padding(innerPadding)
        ) {
            composable(route = PsyhoTestScreen.Start.name) {
                StartScreen(
                    onNextButton = { psychoTest, testScreen ->
                        navController.navigate(testScreen)
                        psychoTestViewModel.selectPsychoTest(psychoTest)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            // Психологические тесты
            composable(route = PsyhoTestScreen.AbilityToEmpathize.name) {
//                val questions = AbilityToEmpathizeData.questions.map { id ->
//                    context.resources.getString(
//                        id
//                    )
//                }
//                val stateOrNull =
//                    psychoTestViewModel.uiStates[PsychoTests.AbilityToEmpathize]!!.collectAsState().value
//                if (stateOrNull == null) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .wrapContentSize(Alignment.Center)
//                    ) {
//                        CircularProgressIndicator1(
//                            modifier = Modifier.width(64.dp),
//                            color = MaterialTheme.colorScheme.secondary,
//                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
//                        )
//                    }
//                } else {
//                    val answersOrNull = stateOrNull.answers
//                    val answers = answersOrNull ?: questions.map { null }
//                    PsyhoTestScreen(
//                        title = stringResource(id = PsyhoTestScreen.AbilityToEmpathize.title),
//                        questions = questions,
//                        answers = answers,
//                        onSelectAnswer = { newAnswer, index ->
//                            val newAnswers =
//                                answers.mapIndexed { ind, answer -> if (ind == index) newAnswer else answer }
//                            psychoTestViewModel.saveTestAnswer(
//                                PsychoTests.AbilityToEmpathize,
//                                newAnswers
//                            )
//                        },
//                        onResetResult = {
//                            psychoTestViewModel.saveTestAnswer(
//                                PsychoTests.AbilityToEmpathize,
//                                null
//                            )
//                        },
//                        isResetTest = answersOrNull != null,
//                        options = AbilityToEmpathizeData.options.map { id -> id },
//                        modifier = Modifier
//                            .fillMaxSize()
//                    )
//                }
            }
        }
    }
}

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    onNextButton: (psychoTest: PsychoTests, testScreen: String) -> Unit = { psychoTests: PsychoTests, s: String -> }
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
                onNextButton(test, PsyhoTestScreen.AbilityToEmpathize.name)
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
                text = "Тесты:",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Column(
                Modifier.padding(10.dp, 0.dp, 0.dp, 0.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                nameList.forEachIndexed { ind, name ->
                    Button(onClick = { onClick(ind) }) {
                        Text(
                            text = name,
                            textAlign = TextAlign.Center
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
                text = name,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                text = description,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
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