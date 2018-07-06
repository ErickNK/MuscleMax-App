package com.flycode.musclemax_app.customUI

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

import com.flycode.musclemax_app.R
import com.flycode.musclemax_app.util.MathUtils

class HeightScalePicker(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    private var maximumAcceptedSize: Int = 0
    private var typeOfUnits: String? = "kg(s)" //TODO: get default string from height_unit array
    private var mHeight: Float = 0.toFloat()
    private var backgroundColor: String? = "#00ffffff"
    private var pointerBackgroundColor: String? = "#ffffff"
    var onHeightChangedListener: HeightScalePicker.OnHeightChangedListener? = null
    private val scaleAdapter: ScaleAdapter
    private val mRecyclerView: RecyclerView
    private val pointer: View
    private val heightScaleSpacer = HeightScaleSpacer()
    private val pointerHeight = (resources.displayMetrics.density * 3).toInt()
    private val pointerWidth = (resources.displayMetrics.density * 150).toInt()
    internal var distance = 0f
    internal var oneUnitHeight: Int = 0

    init {

        //GET ATTRIBUTES
        val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.ScalePicker,
                0, 0)
        try {
            maximumAcceptedSize = a.getInt(R.styleable.ScalePicker_maximumAcceptedSize, 200)
            typeOfUnits = if (a.getString(R.styleable.ScalePicker_typeOfUnits) != null)
                a.getString(R.styleable.ScalePicker_typeOfUnits)
            else
                typeOfUnits
            mHeight = a.getFloat(R.styleable.ScalePicker_mWeight, 0f)
            backgroundColor = if (a.getString(R.styleable.ScalePicker_bgColor) != null)
                a.getString(R.styleable.ScalePicker_bgColor)
            else
                backgroundColor
            pointerBackgroundColor = if (a.getString(R.styleable.ScalePicker_pointerBgColor) != null)
                a.getString(R.styleable.ScalePicker_pointerBgColor)
            else
                pointerBackgroundColor
        } finally {
            a.recycle()
        }

        // Create our internal recycler view and add it to the parent frame layout
        mRecyclerView = RecyclerView(context)
        mRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //SCALE ADAPTER
        scaleAdapter = ScaleAdapter(context, ScaleAdapter.VERTICAL_ORIENTATION)
        scaleAdapter.backgroundColor = Color.parseColor(backgroundColor)
        scaleAdapter.maximumSize = maximumAcceptedSize
        scaleAdapter.typeOfUnit = typeOfUnits
        mRecyclerView.adapter = scaleAdapter
        mRecyclerView.addItemDecoration(heightScaleSpacer)

        mRecyclerView.isHorizontalScrollBarEnabled = false
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {}

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                distance += dy.toFloat()

                if (oneUnitHeight != 0)
                    mHeight = MathUtils.round(distance / oneUnitHeight,1);
                    onHeightChangedListener?.OnHeightChanged(mHeight, typeOfUnits)
            }
        })

        //CREATE POINTER
        pointer = View(context)
        pointer.setBackgroundColor(Color.parseColor(pointerBackgroundColor))
        pointer.layoutParams = FrameLayout.LayoutParams(
                pointerWidth,
                pointerHeight)

        addView(mRecyclerView)
        addView(pointer)
    }

    /**
     * Add spaces to the scale.
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

        if (changed) {
            // Layout the spacers now that we are measured
            val height = height
            val itemOffset = height / 2

            //SET UP SPACERS
            heightScaleSpacer.offset = itemOffset

            //SET UP POINTER
            val layoutParams = pointer.layoutParams as FrameLayout.LayoutParams
            layoutParams.setMargins(0, itemOffset, 0, 0)
            pointer.layoutParams = layoutParams

            //GET ONE UNIT LENGTH
            val view = mRecyclerView.getChildAt(1)
            if (view != null)
                oneUnitHeight = view.height
        }
    }

    /**
     * Cause the recycler view to redraw with the new size
     *
     */
    fun setMaximumAcceptedSize(maximumAcceptedSize: Int) {
        this.maximumAcceptedSize = maximumAcceptedSize
        scaleAdapter.maximumSize = maximumAcceptedSize
        scaleAdapter.notifyDataSetChanged()
    }

    /**
     * Cause the recycler view to redraw with the new units
     *
     */
    fun setTypeOfUnits(typeOfUnits: String) {
        this.typeOfUnits = typeOfUnits
        scaleAdapter.typeOfUnit = typeOfUnits
        scaleAdapter.notifyDataSetChanged()
    }

    fun setMHeight(mHeight: Float) {
        this.mHeight = mHeight
        mRecyclerView.scrollTo((mHeight * oneUnitHeight).toInt(), 0)
    }

    fun getMHeight():Float {
        return mHeight
    }

    /**
     * Interface for listening to changes in height.
     */
    interface OnHeightChangedListener {
        fun OnHeightChanged(mHeight: Float, typeOfUnits: String?)
    }
}
