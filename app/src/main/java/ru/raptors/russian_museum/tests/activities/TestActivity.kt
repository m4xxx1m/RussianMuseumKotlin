package ru.raptors.russian_museum.tests.activities

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import ru.raptors.russian_museum.R
import ru.raptors.russian_museum.databinding.ActivityTestBinding
import ru.raptors.russian_museum.tests.Test
import ru.raptors.russian_museum.tests.TestData

class TestActivity : AppCompatActivity() {
    private var _binding: ActivityTestBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var test: Test
    private lateinit var cards: ArrayList<MaterialCardView>
    private var currentQuestionNum = -1
    private var currentAnswer = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        test = TestData.getCurrentTest()
        binding.backButton.setOnClickListener { finish() }
        binding.textView.text = test.name
        setProgress()
        setQuestionName()
        placeOptions()
        setNextButton()
    }
    
    private fun setProgress() {
        currentQuestionNum++
        binding.questionNumber.text = "Вопрос ${currentQuestionNum+1}/${test.getQuestionsCount()}"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.progressBar.setProgress((100.0f * currentQuestionNum / test
                .getQuestionsCount()).toInt(), true)
        }
        else {
            binding.progressBar.progress = (100.0f * currentQuestionNum / test
                .getQuestionsCount()).toInt()
        }
    }
    
    private fun setQuestionName() {
        binding.question.text = test.getQuestion(currentQuestionNum).question
    }
    
    private fun placeOptions() {
        val placeHolder = binding.placeHolder
        placeHolder.removeAllViews()
        cards = ArrayList(test.getQuestion(currentQuestionNum).getOptionsCount())
        for (i in 0 until test.getQuestion(currentQuestionNum).getOptionsCount()) {
            val option = layoutInflater.inflate(R.layout.test_answer_option, placeHolder, 
                false) as MaterialCardView
            cards.add(option)
            option.findViewById<TextView>(R.id.text_option).text = test.getQuestion(
                currentQuestionNum).getAnswerOption(i)
            option.setBackgroundResource(0)
            option.setOnClickListener { 
                if (currentAnswer != -1)
                    return@setOnClickListener
                currentAnswer = i
                if (test.getQuestion(currentQuestionNum).getCategory(i) > 0) {
                    option.setBackgroundResource(R.drawable.card_right)
                }
                else {
                    vibrate()
                    option.setBackgroundResource(R.drawable.card_wrong)
                    val index = test.getQuestion(currentQuestionNum).getRightAnswerIndex()
                    cards[index].setBackgroundResource(R.drawable.card_right)
                }
                binding.nextButton.visibility = View.VISIBLE
            }
            placeHolder.addView(option)
        }
    }
    
    private fun setNextButton() {
        binding.nextButton.setOnClickListener { 
            binding.nextButton.visibility = View.INVISIBLE
            test.addPoints(currentQuestionNum, currentAnswer)
            currentAnswer = -1
            if (currentQuestionNum + 1 == test.getQuestionsCount()) {
                test.finish()
                finish()
                return@setOnClickListener
            }
            setProgress()
            setQuestionName()
            placeOptions()
        }
    }

    private fun vibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(250, VibrationEffect
                .DEFAULT_AMPLITUDE))
        }
        else {
            vibrator.vibrate(250)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}