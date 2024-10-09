package com.example.psychologicaltest

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.psychologicaltest.data.PsychoTestPreferencesRepository

private const val TEST_PSYCHO_PREFERENCE_NAME = "test_psycho_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = TEST_PSYCHO_PREFERENCE_NAME
)

class PsychologicalTestApplication: Application() {
    lateinit var userPreferencesRepository: PsychoTestPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        userPreferencesRepository = PsychoTestPreferencesRepository(dataStore)
    }
}