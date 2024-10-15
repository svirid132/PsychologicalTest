package com.example.psychologicaltest.data

object PsychoTestToObject {
    private val data = mapOf(
        PsychoTests.AbilityToEmpathize to AbilityToEmpathizeData
    )
    fun getData(test: PsychoTests): TestData {
        return data[test]!!
    }
    fun getList(): List<Pair<PsychoTests, AbilityToEmpathizeData>> {
        return data.toList()
    }
}