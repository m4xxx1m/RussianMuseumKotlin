package ru.raptors.russian_museum.find_object

import kotlin.math.abs

class Line(val a: Point, val b: Point) {
    fun doesIntercept(line: Line): Boolean {
        val x1 = a.x; val y = a.y; var x2 = b.x
        var x3 = line.a.x; var y3 = line.a.y
        var x4 = line.b.x; var y4 = line.b.y

        if (x3 < x1 && x4 < x1)
            return false
        if (y3 < y &&  y4 < y || y3 > y && y4 > y)
            return false
        if (x3 >= x1 && x4 >= x1)
            return true
        if (y3 == y4)
            return true
        if (x3 > x4) {
            var t = x3; x3 = x4; x4 = t
            t = y3; y3 = y4; y4 = t
        }
        val bx = x4 - x1
        if (y == y4)
            return true
        val b2 = (x4 - x3) / (1 + abs(y - y3) / abs(y - y4))
        return bx >= b2
    }
}