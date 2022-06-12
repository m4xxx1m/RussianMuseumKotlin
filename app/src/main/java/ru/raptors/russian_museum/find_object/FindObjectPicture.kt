package ru.raptors.russian_museum.find_object

import java.util.*
import kotlin.collections.ArrayList

class FindObjectPicture(input: String) {
    val title: String
    val width: Int
    val height: Int
    private val numberOfPoints: Int
    private val points: ArrayList<Point>
    var lines: ArrayList<Line>? = null
        get() {
            if (field == null) {
                initLines()
            }
            return field
        }
    var minX = Int.MAX_VALUE
    var maxX = -1
    var minY = Int.MAX_VALUE
    var maxY = -1

    init {
        val sc = Scanner(input)
        width = sc.nextInt()
        height = sc.nextInt()
        numberOfPoints = sc.nextInt()
        points = ArrayList(numberOfPoints)
        for (i in 0 until numberOfPoints) {
            points.add(Point(sc.nextInt(), sc.nextInt()))
        }
        title = sc.nextLine().substring(1)
        setMinAndMax()
    }

    fun getAspectRatio() = width.toFloat() / height

    private fun setMinAndMax() {
        for (p in points) {
            if (p.x > maxX)
                maxX = p.x
            if (p.x < minX)
                minX = p.x
            if (p.y > maxY)
                maxY = p.y
            if (p.y < minY)
                minY = p.y
        }
    }

    private fun initLines() {
        lines = ArrayList(numberOfPoints)
        for (i in 0 until numberOfPoints) {
            lines!!.add(Line(points[i], points[(i + 1) % numberOfPoints]))
        }
    }
}