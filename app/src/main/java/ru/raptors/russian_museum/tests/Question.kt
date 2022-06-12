package ru.raptors.russian_museum.tests

class Question constructor(val question: String, amountOfAnswers: Int) {
    private val answerOptions = ArrayList<String>(amountOfAnswers)
    private val pointsCategory = ArrayList<Int>(amountOfAnswers)

    fun addAnswerOption(category: Int, answer: String) {
        answerOptions.add(answer)
        pointsCategory.add(category)
    }

    fun getOptionsCount() = answerOptions.size

    fun getAnswerOption(index: Int) = answerOptions[index]

    fun getCategory(index: Int) = pointsCategory[index]

    fun getRightAnswerIndex(): Int {
        return if (pointsCategory.contains(0)) {
            pointsCategory.indexOf(1)
        } else -1
    }
}