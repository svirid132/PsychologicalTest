package com.example.psychologicaltest.data

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.psychologicaltest.PsychologicalTestApplication
import com.example.psychologicaltest.ui.PsychoTestViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface TestData {
    val name: Int
    val description: Int
    val questions: List<Int>
    val options: List<Int>
    val optionToScore: Map<Int, Int>
}