package com.flycode.musclemax_app.customUI.StepCounter

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

import com.flycode.musclemax_app.R
import com.flycode.musclemax_app.util.DrawUtils


class Circle : View {

    private var radius: Int = 0
    private var strokeWidth: Int = 0
    private var circleColor: String? = "#ffffff"
    private var circlePaint: Paint = Paint()

//    lateinit var context: Context

    constructor(context: Context) : super(context) {
//        this.context = context
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
//        this.context = context

        //GET ATTRIBUTES
        val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.Circle,
                0, 0)
        try {
            circleColor = if (a.getString(R.styleable.Circle_circleColor) != null)
                a.getString(R.styleable.Circle_circleColor)
            else
                circleColor
            strokeWidth = a.getInt(R.styleable.Circle_strokeWidth, 3)
            radius = a.getInt(R.styleable.Circle_radius, 50)

        } finally {
            a.recycle()
        }

        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
//        this.context = context
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
//        this.context = context
        init()
    }

    private fun init() {
        circlePaint.style = Paint.Style.STROKE
        circlePaint.color = Color.parseColor(circleColor)
        circlePaint.strokeWidth = DrawUtils.convertDpToPixel(strokeWidth.toFloat(), context!!)

        radius = DrawUtils.convertDpToPixel(radius.toFloat(), context!!).toInt()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val x = width
        val y = height
        canvas.drawCircle((x / 2).toFloat(), (y / 2).toFloat(), radius.toFloat(), circlePaint)
    }
}
