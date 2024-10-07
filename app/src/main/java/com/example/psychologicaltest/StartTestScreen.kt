package com.example.psychologicaltest

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.psychologicaltest.data.AbilityToEmpathizeData
import com.example.psychologicaltest.ui.theme.PsychologicalTestTheme
import com.example.psychologicaltest.ui.theme.PsyhoTestScreen
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.currentBackStackEntryAsState

enum class PsyhoTestScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    AbilityToEmpathize(title = R.string.abilities_to_empatnice)
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
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = PsyhoTestScreen.valueOf(
        backStackEntry?.destination?.route ?: PsyhoTestScreen.Start.name
    )
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
                    onNextButton = {
                        navController.navigate(PsyhoTestScreen.AbilityToEmpathize.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            // Психологические тесты
            composable(route = PsyhoTestScreen.AbilityToEmpathize.name) {
                val context = LocalContext.current
                PsyhoTestScreen(
                    title = stringResource(id = PsyhoTestScreen.AbilityToEmpathize.title),
                    questions = AbilityToEmpathizeData.Questions.map { id ->
                        context.resources.getString(
                            id
                        )
                    },
                    options = AbilityToEmpathizeData.Options.map { id -> id },
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun StartScreen(modifier: Modifier = Modifier, onNextButton: () -> Unit = {}) {
    Box(modifier = modifier.fillMaxSize()) {
        Row {
            TestNameList(modifier = Modifier.weight(0.5f))
            InfoTest(modifier = Modifier.weight(0.5f))
        }
        Button(
            onClick = onNextButton,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(0.dp, 0.dp, 20.dp, 20.dp)
        ) {
            Text(text = "Запуск")
        }
    }
}

@Composable
fun TestNameList(modifier: Modifier) {
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
                for (i in 1..3) {
                    Button(onClick = { /*TODO*/ }) {
                        Text(
                            text = "Тесты",
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoTest(modifier: Modifier) {
    Box(modifier = modifier) {
        Column {
            Text(
                text = "Test Name",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test",
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