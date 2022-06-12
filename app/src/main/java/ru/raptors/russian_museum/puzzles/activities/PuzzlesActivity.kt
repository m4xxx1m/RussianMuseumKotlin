package ru.raptors.russian_museum.puzzles.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.raptors.russian_museum.DifficultyLevel
import ru.raptors.russian_museum.R
import ru.raptors.russian_museum.puzzles.Puzzle
import ru.raptors.russian_museum.puzzles.adapters.PuzzleAdapter

class PuzzlesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzles)
        findViewById<View>(R.id.back_button).setOnClickListener { finish() }
        val puzzlesOver14RecyclerView = findViewById<View>(R.id.puzzles_14) as RecyclerView
        val puzzlesUnder14RecyclerView = findViewById<View>(R.id.puzzles_under_14) as RecyclerView
        val puzzlesUnder8RecyclerView = findViewById<View>(R.id.puzzles_under_8) as RecyclerView
        var layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
            false)
        val puzzles14 = ArrayList<Puzzle>()
        puzzles14.add(Puzzle("Девятый вал", "Иван Айвазовский",
                R.drawable.puzzle_14_2, DifficultyLevel.Over14))
        puzzles14.add(Puzzle("Лунная ночь на Днепре", "Архип Куинджи",
                R.drawable.puzzle_14_1, DifficultyLevel.Over14))
        puzzlesOver14RecyclerView.layoutManager = layoutManager
        val puzzleAdapter = PuzzleAdapter(this, puzzles14)
        puzzlesOver14RecyclerView.adapter = puzzleAdapter
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, 
            false)
        val puzzlesUnder14 = ArrayList<Puzzle>()
        puzzlesUnder14.add(Puzzle("Атлеты", "Казимир Малевич",
                R.drawable.puzzle_under_14_1, DifficultyLevel.Under14))
        puzzlesUnder14.add(Puzzle("Красная конница", "Казимир Малевич",
                R.drawable.puzzle_under_14_2, DifficultyLevel.Under14))
        puzzlesUnder14.add(Puzzle("Композиция VIII", "Василий Кандинский",
                R.drawable.puzzle_under_14_3, DifficultyLevel.Under14))
        puzzlesUnder14RecyclerView.layoutManager = layoutManager
        val puzzleUnder14Adapter = PuzzleAdapter(this, puzzlesUnder14)
        puzzlesUnder14RecyclerView.adapter = puzzleUnder14Adapter
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, 
            false)
        val puzzlesUnder8 = ArrayList<Puzzle>()
        puzzlesUnder8.add(Puzzle("Три радости", "Николай Рерих",
                R.drawable.puzzle_under_8_1, DifficultyLevel.Under8))
        puzzlesUnder8.add(Puzzle("Заморские гости", "Николай Рерих",
                R.drawable.puzzle_under_8_2, DifficultyLevel.Under8))
        puzzlesUnder8RecyclerView.layoutManager = layoutManager
        val puzzleUnder8Adapter = PuzzleAdapter(this, puzzlesUnder8)
        puzzlesUnder8RecyclerView.adapter = puzzleUnder8Adapter
        puzzleAdapter.notifyDataSetChanged()
    }
}