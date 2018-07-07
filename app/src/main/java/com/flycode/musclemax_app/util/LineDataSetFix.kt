package com.flycode.musclemax_app.util

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet

class LineDataSetFix(yVals: List<Entry>, label: String) : LineDataSet(yVals, label) {

    override fun getEntryForIndex(index: Int): Entry? {
        if (mValues.size > index) {
            return if (mValues.isEmpty()) null else mValues[index]
        } else {
            //determine by how much
            val byHowMuch = index - (mValues.size - 1)
            return if (mValues.isEmpty()) null else mValues[index - byHowMuch]
        }
    }
}
