package com.flycode.musclemax_app.util

import java.math.BigDecimal
import java.math.RoundingMode

object MathUtils {

    fun round(value: Float, places: Int): Float {
        if (places < 0) throw IllegalArgumentException()

        var bd = BigDecimal(java.lang.Float.toString(value))
        bd = bd.setScale(places, RoundingMode.HALF_UP)
        return bd.toFloat()
    }
}
