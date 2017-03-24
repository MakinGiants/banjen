package com.makingiants.android.banjotuner

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.widget.FrameLayout
import kotlin.properties.Delegates

/**
 * Helps to draw a bitmap on footprintX,footprintY positions when shouldPaintTouchBitmap
 * variable is set to true
 */
class TouchDrawLayout
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private var rightFeetBitmap: Bitmap
    private var leftFeetBitmap: Bitmap
    private var footprintX: Float = 0.toFloat()
    private var footprintY = 100f
    private var middleScreenVertical: Float = 0.toFloat()

    var shouldPaintTouchBitmap by Delegates.observable(false) { d, old, new ->  invalidate() }

    init {
        inflate(context, R.layout.empty_relative_layout, this)
        rightFeetBitmap = BitmapFactory.decodeResource(resources, R.drawable.ear_ic_right)
        leftFeetBitmap = BitmapFactory.decodeResource(resources, R.drawable.ear_ic_left)

        val dm = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(dm)
        middleScreenVertical = (dm.widthPixels / 2).toFloat()
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)

        if (shouldPaintTouchBitmap) {
            if (footprintX < middleScreenVertical) {
                canvas.drawBitmap(leftFeetBitmap, footprintX, footprintY, null)
            } else {
                canvas.drawBitmap(rightFeetBitmap, footprintX, footprintY, null)
            }
        }
    }

    fun setTouch(x: Float, y: Float) {
        footprintX = x - leftFeetBitmap.width / 2
        footprintY = y - leftFeetBitmap.height / 2
        invalidate()
    }
}
