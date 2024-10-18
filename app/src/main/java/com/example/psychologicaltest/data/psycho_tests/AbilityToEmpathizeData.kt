package com.example.psychologicaltest.data.psycho_tests

import com.example.psychologicaltest.R
import com.example.psychologicaltest.data.TestData
import com.example.psychologicaltest.data.TestResult

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

    override fun result(answers: List<Int>): TestResult {
        val plusEmpathy = setOf(1, 13, 22, 25, 28, 34, 40, 46, 49, 52, 55, 70, 75, 78, 80)
        val minusEmpathy = setOf(4, 7, 10, 16, 31, 38, 43, 58, 61, 64, 67, 72, 73, 77, 79, 81, 83)
        val plusAccession = setOf(5, 8, 14, 17, 23, 29, 37, 41, 47, 50, 65, 71, 76)
        val minusAccession = setOf(2, 11, 20, 26, 32, 35, 44, 53, 56, 59, 62, 68, 74)
        val plusRejection = setOf(9, 15, 24, 30, 33, 36, 42, 51, 54, 60, 63, 82)
        val minusRejection = setOf(3, 6, 12, 18, 21, 27, 39, 45, 48, 57, 66, 69)
        var empathy = 0
        var accession = 0
        var rejection = 0
        answers.forEachIndexed { ind, value ->
            val num = ind + 1
            val score = optionToScore[value]
            when {
                plusEmpathy.contains(num) -> {
                    empathy += score!!
                }

                minusEmpathy.contains(num) -> {
                    empathy -= score!!
                }
            }
            when {
                plusAccession.contains(num) -> {
                    accession += score!!
                }

                minusAccession.contains(num) -> {
                    accession -= score!!
                }
            }
            when {
                plusRejection.contains(num) -> {
                    rejection += score!!
                }

                minusRejection.contains(num) -> {
                    rejection -= score!!
                }
            }
        }
        return TestResult(
            "Общий результат теста:",
            "Результат по эмпатии = $empathy\r\n" +
                    "Результат по присоединению = $accession\r\n" +
                    "Результат по отвержению = $rejection\r\n" +
                    "\n" +
                    "   Нормативы для мужчин:\n" +
                    "- если эмпатия больше и равно 11;\n" +
                    "- если присоединение больше и равно 32;\n" +
                    "- если отвержение больше и равно (-4);\n" +
                    "- остальные показатели - показатели неопределенности;\n" +
                    "\n" +
                    "     Нормативы для женщин:\n" +
                    "- если эмпатия больше и равно 17;\n" +
                    "- если присоединение больше и равно 18;\n" +
                    "- если отвержение больше и равно 12;\n" +
                    "- остальные показатели - показатели неопределенности;"
        )
    }
}

