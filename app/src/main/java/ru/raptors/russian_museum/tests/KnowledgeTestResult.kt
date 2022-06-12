package ru.raptors.russian_museum.tests

class KnowledgeTestResult(
    override val text: String,
    val points: Int,
    override val pictureRes: Int) : TestResult() {}