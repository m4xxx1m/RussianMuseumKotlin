package ru.raptors.russian_museum

enum class DifficultyLevel constructor(private val _value: Int) {
    Under8(0),
    Under14(1),
    Over14(2);

    val value get() = _value
    companion object {
        @JvmStatic
        fun getInstance(value: Int) =
            when (value) {
                0 -> Under8
                1 -> Under14
                2 -> Over14
                else -> throw IllegalArgumentException("Wrong Argument")
            }
    }
}