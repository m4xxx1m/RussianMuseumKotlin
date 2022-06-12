package ru.raptors.russian_museum.tests

import android.content.res.TypedArray
import ru.raptors.russian_museum.DifficultyLevel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max

class Test constructor(info_: String, questions_: String, results_: String, pictureRes: TypedArray) {
    lateinit var name: String
        private set
    private var categories: Int = 0
    private var isMegaTest: Boolean = true
    private lateinit var minDifficultyLevel: DifficultyLevel
    private lateinit var maxDifficultyLevel: DifficultyLevel
    private lateinit var questions: ArrayList<Question>
    private lateinit var results: ArrayList<TestResult>
    var isTestSolved = false
        private set
    private var resultIndex = -1
    private val points: ArrayList<Int>

    init {
        parseInfo(info_)
        parseQuestions(questions_)
        parseResults(results_, pictureRes)
        points = ArrayList(max(1, categories))
        for (i in 0 until max(1, categories)) {
            points.add(0)
        }
    }

    private fun parseInfo(info: String) {
        val scanner = Scanner(info)
        categories = scanner.nextInt()
        isMegaTest = categories > 0
        minDifficultyLevel = DifficultyLevel.getInstance(scanner.nextInt())
        maxDifficultyLevel = DifficultyLevel.getInstance(scanner.nextInt())
        name = scanner.nextLine().substring(1)
    }

    private fun parseQuestions(questions_: String) {
        val data = questions_.split("|")
        val amountOfQuestions = data[0].toInt()
        questions = ArrayList(amountOfQuestions)

        var k = 1
        for (i in 0 until amountOfQuestions) {
            val ansCount = data[k+1].toInt()
            questions.add(Question(data[k], ansCount))
            k += 2
            for (j in 0 until ansCount) {
                questions[i].addAnswerOption(data[k].toInt(), data[k+1])
                k += 2
            }
        }
    }

    private fun parseResults(results_: String, picRes: TypedArray) {
        val data = results_.split("|")
        results = ArrayList(data.size)
        for ((i, k) in (data.indices step 2).withIndex()) {
            val res = picRes.getResourceId(i, -1)
            if (isMegaTest) {
                results.add(MegaTestResult(data[k], data[k+1].toInt(), res))
            }
            else {
                results.add(KnowledgeTestResult(data[k], data[k+1].toInt(), res))
            }
        }
    }

    fun finish() {
        if (isMegaTest) {
            var max = 0
            var maxInd = 0
            for (i in points) {
                if (points[i] > max) {
                    max = points[i]
                    maxInd = i
                }
                points[i] = 0
            }
            resultIndex = maxInd
        }
        else {
            for (i in results.indices) {
                val point = (results[i] as KnowledgeTestResult).points
                if (points[0] >= point) {
                    resultIndex = i
                    break
                }
            }
            points[0] = 0
        }
        isTestSolved = true
    }

    fun getResultRes() = results[resultIndex].pictureRes

    fun getResult() = results[resultIndex].text

    fun getQuestionsCount() = questions.size

    fun getQuestion(index: Int) = questions[index]

    fun addPoints(question: Int, answer: Int) {
        val index = questions[question].getCategory(answer) - 1
        if (index >= 0) {
            points[index] = points[index] + 1
        }
    }
}