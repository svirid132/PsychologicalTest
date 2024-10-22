package com.example.psychologicaltest.data

import com.example.psychologicaltest.data.psycho_tests.AbilityToEmpathizeData
import com.example.psychologicaltest.data.psycho_tests.PsychoTests

object PsychoTestToObject {
    private val data = mapOf(
        PsychoTests.AbilityToEmpathize to AbilityToEmpathizeData
    )
    fun getData(test: PsychoTests): TestData {
        return data[test]!!
    }
    fun getList(): List<Pair<PsychoTests, TestData>> {
        return data.toList()
    }
}