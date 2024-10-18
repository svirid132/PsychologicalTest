package com.example.psychologicaltest.ui

import android.content.IntentSender.OnFinished
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.psychologicaltest.PsychologicalTestApplication
import com.example.psychologicaltest.data.PsychoTestPreferencesRepository
import com.example.psychologicaltest.data.PsychoTestToObject
import com.example.psychologicaltest.data.psycho_tests.PsychoTests
import com.example.psychologicaltest.data.TestData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PsychoTestViewModel(
    private val userPreferencesRepository: PsychoTestPreferencesRepository
) : ViewModel() {

    var uiState: StateFlow<PsychoTestUIState?> = MutableStateFlow(null)

    fun selectPsychoTest(test: PsychoTests?) {
        if (test == null) {
            uiState = MutableStateFlow(null)
            return
        }
        val obj: TestData = PsychoTestToObject.getData(test)
        uiState = userPreferencesRepository.psychoTestAnswers[test]!!.map { answerString ->
            val answers: List<Int?>? =
                answerString?.split(",")?.map { it.toIntOrNull() }.let {
                    if(obj.questions.size != it?.size) {
                        return@let null
                    }
                    return@let it
                }
            PsychoTestUIState(currentTest = test, testData = obj, answers = answers)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )
    }

    fun saveTestAnswer(test: PsychoTests, answers: List<Int?>?, onSaved: (() -> Unit)? = null) {
        val answerString: String? = answers?.joinToString(",")
        viewModelScope.launch {
            userPreferencesRepository.saveTestPreference(test, answerString)
            onSaved?.invoke()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PsychologicalTestApplication)
                PsychoTestViewModel(application.userPreferencesRepository)
            }
        }
    }
}

data class PsychoTestUIState(
    val currentTest: PsychoTests,
    val testData: TestData,
    val answers: List<Int?>? = null,
)