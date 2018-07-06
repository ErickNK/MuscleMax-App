package com.flycode.musclemax_app.util

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter

import java.text.SimpleDateFormat
import java.util.Date

class XAxisDateValueFormatter : IAxisValueFormatter {
    override fun getFormattedValue(value: Float, axis: AxisBase): String {
        val date = Date()
        date.time = value.toLong()
        return SimpleDateFormat("dd-MM-yyyy").format(date)
    }
}
