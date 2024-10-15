package com.example.psychologicaltest.data

import androidx.lifecycle.viewModelScope
import com.example.psychologicaltest.R
import com.example.psychologicaltest.ui.PsychoTestUIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

object AbilityToEmpathizeData : TestData {
    override val name = R.string.abilities_to_empatnice_name
    override val description = R.string.abilities_to_empatnice_description
    override val questions = listOf(
        R.string.ability_to_empathize_q1,
        R.string.ability_to_empathize_q2,
        R.string.ability_to_empathize_q3,
        R.string.ability_to_empathize_q4,
        R.string.ability_to_empathize_q5,
        R.string.ability_to_empathize_q6,
        R.string.ability_to_empathize_q7,
        R.string.ability_to_empathize_q8,
        R.string.ability_to_empathize_q9,
        R.string.ability_to_empathize_q10,
        R.string.ability_to_empathize_q11,
        R.string.ability_to_empathize_q12,
        R.string.ability_to_empathize_q13,
        R.string.ability_to_empathize_q14,
        R.string.ability_to_empathize_q15,
        R.string.ability_to_empathize_q16,
        R.string.ability_to_empathize_q17,
        R.string.ability_to_empathize_q18,
        R.string.ability_to_empathize_q19,
        R.string.ability_to_empathize_q20,
        R.string.ability_to_empathize_q21,
        R.string.ability_to_empathize_q22,
        R.string.ability_to_empathize_q23,
        R.string.ability_to_empathize_q24,
        R.string.ability_to_empathize_q25,
        R.string.ability_to_empathize_q26,
        R.string.ability_to_empathize_q27,
        R.string.ability_to_empathize_q28,
        R.string.ability_to_empathize_q29,
        R.string.ability_to_empathize_q30,
        R.string.ability_to_empathize_q31,
        R.string.ability_to_empathize_q32,
        R.string.ability_to_empathize_q33,
        R.string.ability_to_empathize_q34,
        R.string.ability_to_empathize_q35,
        R.string.ability_to_empathize_q36,
        R.string.ability_to_empathize_q37,
        R.string.ability_to_empathize_q38,
        R.string.ability_to_empathize_q39,
        R.string.ability_to_empathize_q40,
        R.string.ability_to_empathize_q41,
        R.string.ability_to_empathize_q42,
        R.string.ability_to_empathize_q43,
        R.string.ability_to_empathize_q44,
        R.string.ability_to_empathize_q45,
        R.string.ability_to_empathize_q46,
        R.string.ability_to_empathize_q47,
        R.string.ability_to_empathize_q48,
        R.string.ability_to_empathize_q49,
        R.string.ability_to_empathize_q50,
        R.string.ability_to_empathize_q51,
        R.string.ability_to_empathize_q52,
        R.string.ability_to_empathize_q53,
        R.string.ability_to_empathize_q54,
        R.string.ability_to_empathize_q55,
        R.string.ability_to_empathize_q56,
        R.string.ability_to_empathize_q57,
        R.string.ability_to_empathize_q58,
        R.string.ability_to_empathize_q59,
        R.string.ability_to_empathize_q60,
        R.string.ability_to_empathize_q61,
        R.string.ability_to_empathize_q62,
        R.string.ability_to_empathize_q63,
        R.string.ability_to_empathize_q64,
        R.string.ability_to_empathize_q65,
        R.string.ability_to_empathize_q66,
        R.string.ability_to_empathize_q67,
        R.string.ability_to_empathize_q68,
        R.string.ability_to_empathize_q69,
        R.string.ability_to_empathize_q70,
        R.string.ability_to_empathize_q71,
        R.string.ability_to_empathize_q72,
        R.string.ability_to_empathize_q73,
        R.string.ability_to_empathize_q74,
        R.string.ability_to_empathize_q75,
        R.string.ability_to_empathize_q76,
        R.string.ability_to_empathize_q77,
        R.string.ability_to_empathize_q78,
        R.string.ability_to_empathize_q79,
        R.string.ability_to_empathize_q80,
        R.string.ability_to_empathize_q81,
        R.string.ability_to_empathize_q82,
        R.string.ability_to_empathize_q83
    )
    override val options = listOf(
        R.string.ability_to_empathize_o1,
        R.string.ability_to_empathize_o2,
        R.string.ability_to_empathize_o3,
        R.string.ability_to_empathize_o4,
        R.string.ability_to_empathize_o5,
        R.string.ability_to_empathize_o6,
        R.string.ability_to_empathize_o7,
        R.string.ability_to_empathize_o8,
        R.string.ability_to_empathize_o9
    )
    override val optionToScore: Map<Int, Int> = mutableMapOf<Int, Int>().apply {
        var score = 4
        options.forEach() { id ->
            val currentScore = score
            --score
            put(id, currentScore)
        }
    }.toMap()
}

