package com.makingiants.android.banjotuner

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.widget.RelativeLayout
import kotlin.properties.Delegates

/**
 * Helps to draw a bitmap on footprintX,footprintY positions when shouldPaintTouchBitmap
 * variable is set to true
 *
 *
 * Created by danielgomez22 on 2/7/15.
 */
class TouchDrawLayout : RelativeLayout {
    private lateinit var rightFeetBitmap: Bitmap
    private lateinit var leftFeetBitmap: Bitmap
    private var footprintX: Float = 0.toFloat()
    private var footprintY = 100f
    private var middleScreenVertical: Float = 0.toFloat()
    var shouldPaintTouchBitmap by Delegates.observable(false) { d, old, new ->
        invalidate()
    }

    //<editor-fold desc="Constructors">
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        inflate(context, R.layout.empty_relative_layout, this)
        rightFeetBitmap = BitmapFactory.decodeResource(resources, R.drawable.ear_ic_right)
        leftFeetBitmap = BitmapFactory.decodeResource(resources, R.drawable.ear_ic_left)

        val dm = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(dm)
        middleScreenVertical = (dm.widthPixels / 2).toFloat()
    }
    //</editor-fold>

    //<editor-fold desc="Activity Overrides">

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

    //</editor-fold>

    fun setTouch(x: Float, y: Float) {
        this.footprintX = x - leftFeetBitmap.width / 2
        this.footprintY = y - leftFeetBitmap.height / 2
        invalidate()
    }
}
