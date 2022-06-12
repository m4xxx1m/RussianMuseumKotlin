package ru.raptors.russian_museum.puzzles

import ru.raptors.russian_museum.DifficultyLevel
import java.io.Serializable

class Puzzle(
    val label: String,
    val author: String,
    val paintingID: Int,
    val difficultyLevel: DifficultyLevel
) : Serializable