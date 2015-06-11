package com.makingiants.android.banjotuner;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RelativeLayout;

/**
 * Helps to draw a bitmap on footprintX,footprintY positions when shouldPaintTouchBitmap
 * variable is set to true
 * <p/>
 * Created by danielgomez22 on 2/7/15.
 */
public class TouchDrawLayout extends RelativeLayout {

    private boolean shouldPaintTouchBitmap = false;
    private Bitmap rightFeetBitmap, leftFeetBitmap;
    private float footprintX, footprintY = 100;
    private float middleScreenVertical;

    //<editor-fold desc="Constructors">

    public TouchDrawLayout(Context context) {
        super(context);
        init();
    }

    public TouchDrawLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TouchDrawLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.empty_relative_layout, this);
        rightFeetBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ear_ic_right);
        leftFeetBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ear_ic_left);

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
        middleScreenVertical = dm.widthPixels / 2;
    }

    //</editor-fold>

    //<editor-fold desc="Activity Overrides">

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (shouldPaintTouchBitmap) {
            if (footprintX < middleScreenVertical) {
                canvas.drawBitmap(leftFeetBitmap, footprintX, footprintY, null);
            } else {
                canvas.drawBitmap(rightFeetBitmap, footprintX, footprintY, null);
            }
        }

    }

    //</editor-fold>

    public void setTouch(float x, float y) {
        this.footprintX = x - (leftFeetBitmap.getWidth() / 2);
        this.footprintY = y - (leftFeetBitmap.getHeight() / 2);
        invalidate();
    }

    public boolean isShouldPaintTouchBitmap() {
        return shouldPaintTouchBitmap;
    }

    public void setShouldPaintTouchBitmap(boolean shouldPaintTouchBitmap) {
        this.shouldPaintTouchBitmap = shouldPaintTouchBitmap;
        invalidate();
    }
}
