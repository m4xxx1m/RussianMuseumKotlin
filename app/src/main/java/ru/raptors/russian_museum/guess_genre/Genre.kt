package ru.raptors.russian_museum.guess_genre

import android.content.res.Resources
import ru.raptors.russian_museum.R

enum class Genre constructor(private val _value: Int) {
    Abstraction(0),
    Allegory(1),
    Animalism(2),
    BattleGenre(3),
    BiblicalStory(4),
    HouseholdGenre(5),
    Illustration(6),
    Interior(7),
    HistoricalPainting(8),
    Caricature(9),
    MythologicalStory(10),
    StillLife(11),
    Landscape(12),
    Portrait(13);

    val value get() = _value
    companion object {
        @JvmStatic
        fun getTotal() = 14

        @JvmStatic
        @Throws(EnumConstantNotPresentException::class)
        fun getInstance(value: Int): Genre =
            when (value) {
                0 -> Abstraction
                1 -> Allegory
                2 -> Animalism;
                3 -> BattleGenre;
                4 -> BiblicalStory;
                5 -> HouseholdGenre;
                6 -> Illustration;
                7 -> Interior;
                8 -> HistoricalPainting;
                9 -> Caricature;
                10 -> MythologicalStory;
                11 -> StillLife;
                12 -> Landscape;
                13 -> Portrait;
                else -> throw EnumConstantNotPresentException(Genre::class.java,
                    "Wrong number of genre")
            }
    }

    fun getString(resources: Resources): String = resources.getStringArray(R.array.genres)[value]

    fun getDescription(res: Resources): String = res.getStringArray(R.array.genres_description)[value]
}