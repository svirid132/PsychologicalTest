package com.example.psychologicaltest.data

import android.content.ContentValues.TAG
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class PsychoTestPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {

    val psychoTestAnswers: Map<PsychoTests, Flow<String?>> = mutableMapOf<PsychoTests, Flow<String?>>().apply {
        enumValues<PsychoTests>().forEach { it ->
            val test: Flow<String?> = dataStore.data
                .catch {
                    if (it is IOException) {
                        Log.e(TAG, "Error reading preferences.", it)
                        emit(emptyPreferences())
                    } else {
                        throw it
                    }
                }
                .map { preferences ->
                    val testKey: Preferences.Key<String> = testPreferences[it.name]!!
                    preferences[testKey]
                }
            put(it, test)
        }
    }.toMap()

    private companion object {
        val testPreferences = mutableMapOf<String, Preferences.Key<String>>()

        init {
            enumValues<PsychoTests>().forEach {
                testPreferences[it.name] = stringPreferencesKey(it.name)
            }
        }
    }

    suspend fun saveTestPreference(psychoTest: PsychoTests, answers: String?) {
        val testKey: Preferences.Key<String> = testPreferences[psychoTest.name]!!
        dataStore.edit { preferences ->
            if (answers == null) {
                preferences.remove(testKey)
                return@edit
            }
            preferences[testKey] = answers
        }
    }
}