package ru.raptors.russian_museum.guess_genre.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ru.raptors.russian_museum.databinding.ActivityChooseGuessGenreBinding

class GuessGenreChooseActivity : AppCompatActivity() {

    private var _binding: ActivityChooseGuessGenreBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChooseGuessGenreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backButton.setOnClickListener { finish() }
        binding.card14.setOnClickListener {
            navigate(2, it)
        }
        binding.card813.setOnClickListener {
            navigate(1, it)
        }
        binding.card68.setOnClickListener {
            navigate(0, it)
        }
    }

    private fun navigate(difficulty: Int, view: View) {
        intent = Intent(this, GuessGenreActivity::class.java)
        intent.putExtra("difficultyLevel", difficulty)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}