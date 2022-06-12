package ru.raptors.russian_museum.puzzles.activities

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import ru.raptors.russian_museum.DifficultyLevel
import ru.raptors.russian_museum.R
import ru.raptors.russian_museum.fragments.DialogGameFinished
import ru.raptors.russian_museum.puzzles.Puzzle
import ru.raptors.russian_museum.puzzles.PuzzlePiece
import ru.raptors.russian_museum.puzzles.TouchListener
import java.util.*
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.roundToInt

class PuzzleActivity : AppCompatActivity() {
    var rowsCount = 1
    var colsCount = 1
    var pieces: ArrayList<PuzzlePiece?>? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle)
        findViewById<View>(R.id.imageButton).setOnClickListener { v: View? -> finish() }
        val layout = findViewById<RelativeLayout>(R.id.puzzlesLayout)
        val imageView = findViewById<ImageView>(R.id.puzzle)
        val label = findViewById<TextView>(R.id.label)
        val author = findViewById<TextView>(R.id.author)
        val intent = intent
        val puzzle = intent.getSerializableExtra("puzzle") as Puzzle?
        if (puzzle != null) {
            when (puzzle.difficultyLevel) {
                DifficultyLevel.Under8 -> {
                    rowsCount = 3
                    colsCount = 4
                }
                DifficultyLevel.Under14 -> {
                    rowsCount = 5
                    colsCount = 7
                }
                DifficultyLevel.Over14 -> {
                    rowsCount = 8
                    colsCount = 10
                }
            }
            var newLabel = ""
            val labelWords = puzzle.label.split(" ").toTypedArray()
            for (i in labelWords.indices) {
                newLabel += labelWords[i]
                if (i == labelWords.size - 1) break
                newLabel += if (i != labelWords.size / 2) { " " } else "\n"
            }
            label.text = newLabel
            author.text = puzzle.author
        }

        // запускаем код, связанный с изображением, после того, как view было создано
        // для расчета всех размеров
        imageView.post {
            if (puzzle != null) {
                val targetW = imageView.width
                val targetH = imageView.height
                val bmOptions = BitmapFactory.Options()
                bmOptions.inJustDecodeBounds = true
                val photoW = bmOptions.outWidth
                val photoH = bmOptions.outHeight
                val scaleFactor = min(photoW / targetW, photoH / targetH)
                bmOptions.inJustDecodeBounds = false
                bmOptions.inSampleSize = scaleFactor
                bmOptions.inPurgeable = true
                val bitmap = BitmapFactory.decodeResource(resources, puzzle.paintingID, bmOptions)
                imageView.setImageBitmap(bitmap)
            }
            pieces = splitImage(rowsCount, colsCount)
            val touchListener = TouchListener(this@PuzzleActivity)
            // пазлы в случайном порядке
            pieces!!.shuffle()
            for (piece in pieces!!) {
                piece!!.setOnTouchListener(touchListener)
                layout.addView(piece)
                // случайное положение в нижней части экрана
                val lParams = piece.layoutParams as RelativeLayout.LayoutParams
                lParams.leftMargin = Random().nextInt(layout.width - piece.pieceWidth)
                lParams.topMargin = layout.height - piece.pieceHeight
                piece.layoutParams = lParams
            }
        }
    }

    fun checkGameOver() {
        if (isGameOver) {
            val dialog: DialogFragment = DialogGameFinished.newInstance(
                getString(R.string.you_solved_puzzles))
            dialog.show(supportFragmentManager, "dialog")
        }
    }

    private val isGameOver: Boolean
        get() {
            for (piece in pieces!!) {
                if (piece!!.canMove) {
                    return false
                }
            }
            return true
        }

    private fun splitImage(rows: Int, cols: Int): ArrayList<PuzzlePiece?> {
        val piecesNumber = rows * cols
        val imageView = findViewById<ImageView>(R.id.puzzle)
        val pieces = ArrayList<PuzzlePiece?>(piecesNumber)

        // Получаем Картину нужного размера
        val drawable = imageView.drawable as BitmapDrawable
        val bitmap = drawable.bitmap
        val dimensions = getBitmapPositionInsideImageView(imageView)
        val scaledBitmapLeft = dimensions[0]
        val scaledBitmapTop = dimensions[1]
        val scaledBitmapWidth = dimensions[2]
        val scaledBitmapHeight = dimensions[3]
        val croppedImageWidth = scaledBitmapWidth - 2 * abs(scaledBitmapLeft)
        val croppedImageHeight = scaledBitmapHeight - 2 * abs(scaledBitmapTop)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledBitmapWidth, 
            scaledBitmapHeight, true)
        val croppedBitmap = Bitmap.createBitmap(scaledBitmap, abs(scaledBitmapLeft), 
            abs(scaledBitmapTop), croppedImageWidth, croppedImageHeight)

        // Измеряем ширину и высоту пазлов
        val pieceWidth = croppedImageWidth / cols
        val pieceHeight = croppedImageHeight / rows

        // Создаём картинки пазлов и добавляем в массив
        var yCoord = 0
        for (row in 0 until rows) {
            var xCoord = 0
            for (col in 0 until cols) {
                var offsetX = 0
                var offsetY = 0
                if (col > 0) {
                    offsetX = pieceWidth / 3
                }
                if (row > 0) {
                    offsetY = pieceHeight / 3
                }

                // применяем смещение для каждого пазла
                val pieceBitmap = Bitmap.createBitmap(croppedBitmap, xCoord - offsetX, 
                    yCoord - offsetY, pieceWidth + offsetX, pieceHeight + offsetY)
                val piece = PuzzlePiece(applicationContext)
                piece.setImageBitmap(pieceBitmap)
                piece.xCoord = xCoord - offsetX + imageView.left
                piece.yCoord = yCoord - offsetY + imageView.top
                piece.pieceWidth = pieceWidth + offsetX
                piece.pieceHeight = pieceHeight + offsetY

                // получаем финальное изображение пазлика
                val puzzlePiece = Bitmap.createBitmap(pieceWidth + offsetX, 
                    pieceHeight + offsetY, Bitmap.Config.ARGB_8888)

                // рисуем стыковки с другими пазлами
                val bumpSize = pieceHeight / 4
                val canvas = Canvas(puzzlePiece)
                val path = Path()
                path.moveTo(offsetX.toFloat(), offsetY.toFloat())
                if (row == 0) {
                    // верхние пазлы
                    path.lineTo(pieceBitmap.width.toFloat(), offsetY.toFloat())
                } else {
                    // верхняя стыковка
                    path.lineTo((offsetX + (pieceBitmap.width - offsetX) / 3).toFloat(),
                        offsetY.toFloat())
                    path.cubicTo((offsetX + (pieceBitmap.width - offsetX) / 6).toFloat(), 
                        (offsetY - bumpSize).toFloat(), 
                        (offsetX + (pieceBitmap.width - offsetX) / 6 * 5).toFloat(),
                        (offsetY - bumpSize).toFloat(),
                        (offsetX + (pieceBitmap.width - offsetX) / 3 * 2).toFloat(),
                        offsetY.toFloat())
                    path.lineTo(pieceBitmap.width.toFloat(), offsetY.toFloat())
                }
                if (col == cols - 1) {
                    // правые пазлы
                    path.lineTo(pieceBitmap.width.toFloat(), pieceBitmap.height.toFloat())
                } else {
                    // правая стыковка
                    path.lineTo(pieceBitmap.width.toFloat(),
                        (offsetY + (pieceBitmap.height - offsetY) / 3).toFloat())
                    path.cubicTo((pieceBitmap.width - bumpSize).toFloat(),
                        (offsetY + (pieceBitmap.height - offsetY) / 6).toFloat(),
                        (pieceBitmap.width - bumpSize).toFloat(),
                        (offsetY + (pieceBitmap.height - offsetY) / 6 * 5).toFloat(),
                        pieceBitmap.width.toFloat(),
                        (offsetY + (pieceBitmap.height - offsetY) / 3 * 2).toFloat())
                    path.lineTo(pieceBitmap.width.toFloat(), pieceBitmap.height.toFloat())
                }
                if (row == rows - 1) {
                    // нижние пазлы
                    path.lineTo(offsetX.toFloat(), pieceBitmap.height.toFloat())
                } else {
                    // нижняя стыковка
                    path.lineTo((offsetX + (pieceBitmap.width - offsetX) / 3 * 2).toFloat(),
                        pieceBitmap.height.toFloat())
                    path.cubicTo((offsetX + (pieceBitmap.width - offsetX) / 6 * 5).toFloat(),
                        (pieceBitmap.height - bumpSize).toFloat(),
                        (offsetX + (pieceBitmap.width - offsetX) / 6).toFloat(),
                        (pieceBitmap.height - bumpSize).toFloat(),
                        (offsetX + (pieceBitmap.width - offsetX) / 3).toFloat(),
                        pieceBitmap.height.toFloat())
                    path.lineTo(offsetX.toFloat(), pieceBitmap.height.toFloat())
                }
                if (col == 0) {
                    // левые пазлы
                    path.close()
                } else {
                    // левая стыковка
                    path.lineTo(offsetX.toFloat(), 
                        (offsetY + (pieceBitmap.height - offsetY) / 3 * 2).toFloat())
                    path.cubicTo((offsetX - bumpSize).toFloat(),
                        (offsetY + (pieceBitmap.height - offsetY) / 6 * 5).toFloat(),
                        (offsetX - bumpSize).toFloat(),
                        (offsetY + (pieceBitmap.height - offsetY) / 6).toFloat(),
                        offsetX.toFloat(),
                        (offsetY + (pieceBitmap.height - offsetY) / 3).toFloat())
                    path.close()
                }

                // маска для пазла
                val paint = Paint()
                paint.color = -0x1000000
                paint.style = Paint.Style.FILL
                canvas.drawPath(path, paint)
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
                canvas.drawBitmap(pieceBitmap, 0f, 0f, paint)

                // рисуем белую границу
                var border = Paint()
                border.color = -0x7f000001
                border.style = Paint.Style.STROKE
                border.strokeWidth = 8.0f
                canvas.drawPath(path, border)

                // рисуем чёрную границу
                border = Paint()
                border.color = 0x80000000.toInt()
                border.style = Paint.Style.STROKE
                border.strokeWidth = 3.0f
                canvas.drawPath(path, border)

                // устанавливаем полученное изображение на пазл
                piece.setImageBitmap(puzzlePiece)
                pieces.add(piece)
                xCoord += pieceWidth
            }
            yCoord += pieceHeight
        }
        return pieces
    }

    private fun getBitmapPositionInsideImageView(imageView: ImageView?): IntArray {
        val ret = IntArray(4)
        if (imageView == null || imageView.drawable == null) return ret

        // Получаем размер картины
        // Получаем значения матрицы картины и помещаем их в массив
        val f = FloatArray(9)
        imageView.imageMatrix.getValues(f)

        // Извлекаем значения масштаба, используя константы (если соотношение сторон сохраняется, scaleX == scaleY)
        val scaleX = f[Matrix.MSCALE_X]
        val scaleY = f[Matrix.MSCALE_Y]

        // Получаем картину (можно также получить растровое изображение за рисунком и getWidth/getHeight)
        val d = imageView.drawable
        val origW = d.intrinsicWidth
        val origH = d.intrinsicHeight

        // Рассчитываем фактические размеры
        val actW = (origW * scaleX).roundToInt()
        val actH = (origH * scaleY).roundToInt()
        ret[2] = actW
        ret[3] = actH

        // Получаем позицию картины
        // Предполагаем, что изображение центрировано в ImageView
        val imgViewW = imageView.width
        val imgViewH = imageView.height
        val top = (imgViewH - actH) / 2
        val left = (imgViewW - actW) / 2
        ret[0] = left
        ret[1] = top
        return ret
    }
}