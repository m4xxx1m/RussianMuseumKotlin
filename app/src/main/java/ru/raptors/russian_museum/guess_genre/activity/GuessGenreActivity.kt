package ru.raptors.russian_museum.guess_genre.activity

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.card.MaterialCardView
import ru.raptors.russian_museum.DifficultyLevel
import ru.raptors.russian_museum.R
import ru.raptors.russian_museum.databinding.ActivityGuessGenreBinding
import ru.raptors.russian_museum.fragments.DialogGameFinished
import ru.raptors.russian_museum.guess_genre.Genre
import java.util.*
import kotlin.collections.ArrayList

class GuessGenreActivity : AppCompatActivity() {

    private var _binding: ActivityGuessGenreBinding? = null
    private val binding get() = _binding!!

    private var difficultyLevel: DifficultyLevel? = null
    private var taskNum = -1
    private lateinit var cards: ArrayList<MaterialCardView>
    private var genres: ArrayList<Genre>? = null
    private var rightAnswerIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityGuessGenreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState != null) {
            restoreData(savedInstanceState)
        }
        binding.backButton.setOnClickListener { finish() }
        if (difficultyLevel == null) {
            difficultyLevel = DifficultyLevel.getInstance(intent.getIntExtra(
                "difficultyLevel", -1))
        }
        if (taskNum < 0) {
            taskNum = getTask()
        }
        val drawable = getDrawable()
        val imageView = binding.image
        imageView.setImageDrawable(drawable)
        cards = ArrayList(getAmountOfTasks())
        if (genres == null) {
            genres = ArrayList()
            generateGenresArray()
        }
        placeOptions()
    }

    private fun restoreData(savedInstanceState: Bundle) {
        taskNum = savedInstanceState.getInt("taskNum", -1)
        difficultyLevel = DifficultyLevel.getInstance(savedInstanceState.getInt(
            "difficultyLevel", -1))
        genres = ArrayList(getAmountOfTasks())
        fromIntToGenres(savedInstanceState.getIntegerArrayList("genres")!!)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("taskNum", taskNum)
        outState.putInt("difficultyLevel", difficultyLevel!!.value)
        outState.putIntegerArrayList("genres", fromGenresToInt())
        super.onSaveInstanceState(outState)
    }

    private fun getTask(): Int {
        val resources = intArrayOf(R.integer.total_6_8, R.integer.total_8_13, R.integer.total_14)
        val totalTask = this.resources.getInteger(resources[difficultyLevel!!.value])
        return Random().nextInt(totalTask)
    }

    private fun getDrawable(): Drawable? {
        val ageRes = intArrayOf(R.array.pictures_6_8, R.array.pictures_8_13, R.array.pictures_14)
        val typedArray = resources.obtainTypedArray(ageRes[difficultyLevel!!.value])
        val res = typedArray.getResourceId(taskNum, -1)
        typedArray.recycle()
        return AppCompatResources.getDrawable(this, res)
    }

    private fun fromGenresToInt(): ArrayList<Int> {
        val genresInt = ArrayList<Int>(getAmountOfTasks())
        for (genre in genres!!) {
            genresInt.add(genre.value)
        }
        return genresInt
    }

    private fun fromIntToGenres(genresInt: ArrayList<Int>) {
        for (genre in genresInt) {
            genres?.add(Genre.getInstance(genre))
        }
    }

    private fun generateGenresArray() {
        val rightAnswer = getRightAnswer()
        val amount = getAmountOfTasks()
        val genresInt = ArrayList<Int>(amount)
        genresInt.add(rightAnswer)
        val random = Random()
        while (genresInt.size < amount) {
            val randomValue = random.nextInt(Genre.getTotal())
            if (!genresInt.contains(randomValue))
                genresInt.add(randomValue)
        }
        genresInt.shuffle()
        fromIntToGenres(genresInt)
        rightAnswerIndex = genresInt.indexOf(rightAnswer)
    }

    private fun placeOptions() {
        val placeHolder = binding.placeHolder
        val inflater = layoutInflater
        for (i in 0 until getAmountOfTasks()) {
            cards.add(inflater.inflate(R.layout.guess_genre_option, placeHolder, false)
                    as MaterialCardView)
            cards[i].findViewById<TextView>(R.id.text_option).text = "${i+1}. ${genres
                    ?.get(i)?.getString(resources)}"
            cards[i].setOnClickListener {
                if (i == rightAnswerIndex) {
                    it.setBackgroundResource(R.drawable.card_right)
                    showDialog()
                }
                else {
                    it.setBackgroundResource(R.drawable.card_wrong)
                    vibrate()
                }
            }
            placeHolder.addView(cards[i])
        }
    }

    private fun showDialog() {
        val dialog = DialogGameFinished.newInstance(getString(R.string.you_guessed_genre))
        dialog.show(supportFragmentManager, "dialog")
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

    private fun getRightAnswer(): Int {
        val res = intArrayOf(R.array.right_answers_6_8, R.array.right_answers_8_13,
            R.array.right_answers_14)
        return resources.getIntArray(res[difficultyLevel!!.value])[taskNum]
    }

    private fun getAmountOfTasks() =
        when (difficultyLevel) {
            DifficultyLevel.Under8 -> 2
            DifficultyLevel.Under14 -> 4
            DifficultyLevel.Over14 -> 6
            else -> throw EnumConstantNotPresentException(DifficultyLevel::class.java,
                "There's no such difficulty level")
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}