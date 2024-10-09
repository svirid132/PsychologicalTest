package com.example.psychologicaltest.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.psychologicaltest.PsychologicalTestApplication
import com.example.psychologicaltest.data.PsychoTestPreferencesRepository
import com.example.psychologicaltest.data.PsychoTests
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PsychoTestViewModel(
    private val userPreferencesRepository: PsychoTestPreferencesRepository
) : ViewModel() {

    private val _uiStates = mutableMapOf<PsychoTests, MutableStateFlow<PsychoTestUIState>>().apply {
        enumValues<PsychoTests>().forEach {
            put(it, MutableStateFlow(PsychoTestUIState()))
        }
    }.toMap()

    val uiStates: Map<PsychoTests, StateFlow<PsychoTestUIState>> =
        mutableMapOf<PsychoTests, StateFlow<PsychoTestUIState>>().apply {
            enumValues<PsychoTests>().forEach { it ->
                val state = userPreferencesRepository.psychoTestAnswers[it]!!.map { answerString ->
                    val answers: List<Int?>? =
                        answerString?.split(",")?.map { it.toIntOrNull() }
                    PsychoTestUIState(answers = answers)
                }
                    .stateIn(
                        scope = viewModelScope,
                        started = SharingStarted.WhileSubscribed(5_000),
                        initialValue = PsychoTestUIState()
                    )
                put(it, state)
            }
        }.toMap()

    init {
        enumValues<PsychoTests>().forEach { it ->
            uiStates[it]
            val state = userPreferencesRepository.psychoTestAnswers[it]!!.map { answerString ->
                val answers: List<Int?>? =
                    answerString?.split(",")?.map { it.toIntOrNull() }
                PsychoTestUIState(answers = answers)
            }
        }
    }

    fun getPsychoTestUI(test: PsychoTests): Flow<PsychoTestUIState> {
        return userPreferencesRepository.psychoTestAnswers[test]!!.map { answerString ->
            val answers: List<Int?>? =
                answerString?.split(",")?.map { it.toIntOrNull() }
            PsychoTestUIState(answers = answers)
        }
    }

//    val modelState: StateFlow<PsychoTestUIState> =
//        MutableStateFlow<PsychoTestUIState>(PsychoTestUIState()).apply {
//            Log.d("answer_101", "qwerty asdf")
//            val backendState: Flow<PsychoTestUIState> = flow {
//                userPreferencesRepository.psychoTestAnswers[PsychoTests.AbilityToEmpathize]!!.map { answerString ->
//                    Log.d("answer_101", answerString ?: "null")
//                    val answers: List<Int?>? =
//                        answerString?.split(",")?.map { it.toIntOrNull() }
//                    PsychoTestUIState(answers)
//                }
//            }
//            val state: StateFlow<PsychoTestUIState> = backendState
//                .stateIn(
//                    scope = viewModelScope,
//                    started = SharingStarted.WhileSubscribed(5_000),
//                    initialValue = PsychoTestUIState()
//                )
//            state
//        }

    fun saveTestAnswer(test: PsychoTests, answers: List<Int?>?) {
        val answerString: String? = answers?.joinToString(",")
        viewModelScope.launch {
            userPreferencesRepository.saveTestPreference(test, answerString)
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
    val answers: List<Int?>? = null
)