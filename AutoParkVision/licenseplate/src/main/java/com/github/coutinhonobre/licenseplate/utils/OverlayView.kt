package com.github.coutinhonobre.licenseplate.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.github.coutinhonobre.licenseplate.R

internal class OverlayView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var results = listOf<BoundingBox>()
    private var boxPaint = Paint()
    private var textBackgroundPaint = Paint()
    private var textPaint = Paint()

    private var bounds = Rect()

    init {
        initPaints()
    }

    fun clear() {
        results = listOf()
        textPaint.reset()
        textBackgroundPaint.reset()
        boxPaint.reset()
        invalidate()
        initPaints()
    }

    private fun initPaints() {
        textBackgroundPaint.color = Color.BLACK
        textBackgroundPaint.style = Paint.Style.FILL
        textBackgroundPaint.textSize = 50f

        textPaint.color = Color.WHITE
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 50f

        boxPaint.color = ContextCompat.getColor(context!!, R.color.bounding_box_color)
        boxPaint.strokeWidth = 8F
        boxPaint.style = Paint.Style.STROKE
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        results.forEach {
            val left = it.x1 * width
            val top = it.y1 * height
            val right = it.x2 * width
            val bottom = it.y2 * height

            // Desenhar o bounding box
            canvas.drawRect(left, top, right, bottom, boxPaint)
            val drawableText = it.clsName

            // Calcular as dimensões do texto
            textBackgroundPaint.getTextBounds(drawableText, 0, drawableText.length, bounds)
            val textWidth = bounds.width()
            val textHeight = bounds.height()

            // Calcular a nova posição do retângulo de fundo do texto (acima do bounding box)
            val textRectLeft = left
            val textRectBottom = top - BOUNDING_RECT_TEXT_PADDING // Base do texto logo acima do bounding box
            val textRectTop = textRectBottom - textHeight - BOUNDING_RECT_TEXT_PADDING
            val textRectRight = left + textWidth + BOUNDING_RECT_TEXT_PADDING * 2

            // Desenhar o retângulo de fundo do texto
            canvas.drawRect(
                textRectLeft,
                textRectTop,
                textRectRight,
                textRectBottom,
                textBackgroundPaint
            )

            // Desenhar o texto dentro do retângulo (acima do bounding box)
            canvas.drawText(
                drawableText,
                textRectLeft + BOUNDING_RECT_TEXT_PADDING,
                textRectBottom - BOUNDING_RECT_TEXT_PADDING,  // Ajustar para alinhar corretamente
                textPaint
            )
        }
    }


    fun setResults(boundingBoxes: List<BoundingBox>) {
        results = boundingBoxes
        invalidate()
    }

    companion object {
        private const val BOUNDING_RECT_TEXT_PADDING = 8
    }
}