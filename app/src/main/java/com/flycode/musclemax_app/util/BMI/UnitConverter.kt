package com.flycode.musclemax_app.util.BMI

object UnitConverter {
    //convert height
    //inches to metres
    fun convert_from_inches_to_metres(value: Float): Float {
        //1 inch = 0.0254 metres
        val convert = 0.0254f
        return convert * value
    }

    //feet to metres
    fun convert_from_feat_to_metres(value: Float): Float {
        //1 feet = 0.3048 metres
        val convert = 0.3048f
        return convert * value
    }

    //convert weight from pounds to kilos
    fun convert_from_pounds_to_kilograms(value: Float): Float {
        //1 pound = 0.454 kilogram
        val convert = 0.453592f
        return convert * value
    }

    fun convert_from_ounce_to_kilograms(value: Float): Float {
        val covert = 0.028350f
        return covert * value
    }

    fun convert_from_centimeters_to_metres(value: Float): Float {
        val covert = 100f
        return value / covert
    }

    fun convert_from_stone_to_kilograms(value: Float): Float {
        val covert = 6.35029f
        return value * covert
    }
}
