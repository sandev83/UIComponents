package com.sandev.pinkeyboard

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout

class SquareFrameLayout: FrameLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        if (widthSize == 0 && heightSize == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            val minSize = Math.min(measuredWidth, measuredHeight)
            setMeasuredDimension(minSize, minSize)
            Log.d("SquareFrame", "0&0 - $id : [$widthSize : $heightSize] => [$minSize]")
        } else {
            val size = if (widthSize == 0 || heightSize == 0) {
                Math.max(widthSize, heightSize)
            } else {
                Math.min(widthSize, heightSize)
            }
            val newMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY)
            super.onMeasure(newMeasureSpec, newMeasureSpec)
            setMeasuredDimension(newMeasureSpec, newMeasureSpec)
            Log.d("SquareFrame", "$widthMeasureSpec & $heightMeasureSpec - $id : [$widthSize : $heightSize] => [$newMeasureSpec]")
        }
    }
}