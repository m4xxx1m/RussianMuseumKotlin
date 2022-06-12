package ru.raptors.russian_museum.puzzles.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import ru.raptors.russian_museum.DifficultyLevel
import ru.raptors.russian_museum.R
import ru.raptors.russian_museum.puzzles.Puzzle
import ru.raptors.russian_museum.puzzles.activities.PuzzleActivity
import ru.raptors.russian_museum.puzzles.activities.PuzzlesActivity

class PuzzleAdapter(private val context: Context, private val puzzles: List<Puzzle>) :
    RecyclerView.Adapter<PuzzleAdapter.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.puzzle, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val puzzle = puzzles[position]
        val res = context.resources
        holder.puzzleLabel.text = puzzle.label
        holder.puzzleAuthor.text = puzzle.author
        holder.puzzleImage.setImageDrawable(ResourcesCompat.getDrawable(
            res, puzzle.paintingID, null))
        when (puzzle.difficultyLevel) {
            DifficultyLevel.Over14 -> holder.gradient.background =
                ResourcesCompat.getDrawable(res, R.drawable.age_14_gradient, null)
            DifficultyLevel.Under14 -> holder.gradient.background =
                ResourcesCompat.getDrawable(res, R.drawable.age_8_13_gradient_2, null)
            DifficultyLevel.Under8 -> holder.gradient.background =
                ResourcesCompat.getDrawable(res, R.drawable.age_6_8_gradient_2, null)
        }
        holder.puzzleLayout.setOnClickListener {
            val intent = Intent(context, PuzzleActivity::class.java)
            intent.putExtra("puzzle", puzzle)
            context.startActivity(intent)
            (context as PuzzlesActivity).finish()
        }
    }

    override fun getItemCount() = puzzles.size

    class ViewHolder internal constructor(val view: View) : RecyclerView.ViewHolder(
        view
    ) {
        val puzzleImage: ImageView = view.findViewById(R.id.puzzleImage)
        val puzzleLabel: TextView = view.findViewById(R.id.puzzleLabel)
        val puzzleAuthor: TextView = view.findViewById(R.id.puzzleAuthor)
        val puzzleLayout: FrameLayout = view.findViewById(R.id.puzzleLayout)
        //val backButton: ImageButton = view.findViewById(R.id.back_button)
        val gradient: View = view.findViewById(R.id.gradient)
    }
}