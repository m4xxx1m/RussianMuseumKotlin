package ru.raptors.russian_museum.find_object

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.MotionEvent
import android.view.View
import ru.raptors.russian_museum.R
import ru.raptors.russian_museum.find_object.activities.FindObjectActivity
import java.util.*
import kotlin.math.roundToInt

class FindObjectView constructor(context: Context) : View(context) {
    private val paint = Paint()
    private var displayWidth = 0
    private var displayHeight = 0
    private lateinit var picture: Bitmap
    private lateinit var findObjectData: FindObjectPicture

    private var maxWidth = 0
    private var maxHeight = 0
    private var finalWidth = 0
    private var finalHeight = 0
    private lateinit var startPoint: Point

    private var lastTouchTime = -1L
    private val timeBetweenTouches = 2000

    constructor(context: Context, findObjectData: FindObjectPicture, taskNum: Int): this(context) {
        val typedArray = resources.obtainTypedArray(R.array.find_object_pictures)
        val res = typedArray.getResourceId(taskNum, -1)
        picture = BitmapFactory.decodeResource(resources, res)
        typedArray.recycle()
        this.findObjectData = findObjectData
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        displayWidth = width
        displayHeight = height
        normalizeBitmap()
        startPoint = getStartPoint()
        canvas?.drawBitmap(picture, startPoint.x.toFloat(), startPoint.y.toFloat(), paint)
        setOnTouchListener(OnTouchListener { view, motionEvent ->
            performClick()
            onTouch(view, motionEvent)
            return@OnTouchListener false
        })
    }

    private fun normalizeBitmap() {
        maxWidth = displayWidth
        maxHeight = displayHeight
        val maxAspectRatio = maxWidth.toFloat() / maxHeight
        if (maxAspectRatio >= findObjectData.getAspectRatio()) {
            finalHeight = maxHeight
            finalWidth = (finalHeight * findObjectData.getAspectRatio()).roundToInt()
        }
        else {
            finalWidth = maxWidth
            finalHeight = (finalWidth.toFloat() / findObjectData.getAspectRatio()).roundToInt()
        }
        picture = Bitmap.createScaledBitmap(picture, finalWidth, finalHeight, false)
    }

    private fun getStartPoint(): Point {
        val startPoint = Point(0, 0)
        val x = (maxWidth - finalWidth) / 2
        val y = (maxHeight - finalHeight) / 2
        startPoint.x += x
        startPoint.y += y
        return startPoint
    }

    private fun wrongAnswer() {
        vibrate()
        lastTouchTime = Calendar.getInstance().time.time;
        (context as FindObjectActivity).wrongAnswer()
    }

    private fun rightAnswer() {
        (context as FindObjectActivity).showDialog()
    }

    private fun vibrate() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(250, VibrationEffect
                .DEFAULT_AMPLITUDE))
        }
        else {
            vibrator.vibrate(250)
        }
    }

    private fun onTouch(view: View, motionEvent: MotionEvent) {
        if (lastTouchTime > 0 && Calendar.getInstance().time.time - lastTouchTime <
                timeBetweenTouches)
            return
        var x = motionEvent.x.roundToInt()
        var y = motionEvent.y.roundToInt()
        x -= startPoint.x
        y -= startPoint.y
        if (x < 0 || y < 0 || x > finalWidth || y > finalHeight)
            return
        x = (x.toFloat() * findObjectData.width / finalWidth).roundToInt()
        y = (y.toFloat() * findObjectData.height / finalHeight).roundToInt()
        if (x < findObjectData.minX || x > findObjectData.maxX || y < findObjectData.minY ||
                y > findObjectData.maxY) {
            wrongAnswer()
            return
        }
        val a = Point(x, y)
        val b = Point(findObjectData.maxX + 1, y)
        val line = Line(a, b)
        var interceptCount = 0
        for (l in findObjectData.lines!!) {
            if (line.doesIntercept(l)) {
                interceptCount++
            }
        }
        if (interceptCount % 2 == 0) {
            wrongAnswer()
        }
        else {
            rightAnswer()
        }
    }
}