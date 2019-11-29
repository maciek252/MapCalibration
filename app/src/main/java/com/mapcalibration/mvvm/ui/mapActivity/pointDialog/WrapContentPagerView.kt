package com.mapcalibration.mvvm.ui.mapActivity.pointDialog

import android.content.Context

import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager

/**
 * Created by Magdiel Lorenzo on 5/20/17.
 */
class WrapContentViewPager : ViewPager {

    constructor(context: Context) : super(context)

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var height = 0

        /* Determine the height of the largest child and
         * use that height as the height of the ViewPager
         */
        for (i in 0..childCount - 1) {
            val child = getChildAt(i)
            child.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            val h = child.measuredHeight
            if (h > height) {
                height = h
            }
        }

        val heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)

        super.onMeasure(widthMeasureSpec, heightSpec)
    }
}