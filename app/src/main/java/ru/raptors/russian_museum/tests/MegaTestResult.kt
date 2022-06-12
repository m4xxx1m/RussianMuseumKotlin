package ru.raptors.russian_museum.tests

class MegaTestResult(
    override val text: String,
    val category: Int,
    override val pictureRes: Int) : TestResult() {}