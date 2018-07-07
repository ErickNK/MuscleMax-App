package com.flycode.musclemax_app.util.BMI

class BMICalculator {

    private var Height: Float = 0.toFloat()
    private var Weight: Float = 0.toFloat()

    /**
     * Set height to be used
     */
    fun setHeight(value: Float, unit: String) {
        when (unit) {
            "cm(s)" //centimeters
            -> Height = UnitConverter.convert_from_centimeters_to_metres(value)
            "ft" //feet
            -> Height = UnitConverter.convert_from_feat_to_metres(value)
            "in(s)" //Inches
            -> Height = UnitConverter.convert_from_inches_to_metres(value)
        }
    }

    /**
     * Set weight to be used
     */
    fun setWeight(value: Float, unit: String) {
        when (unit) {
            "kg(s)" //kgs
            -> Weight = value
            "lb(s)" //pounds
            -> Weight = UnitConverter.convert_from_pounds_to_kilograms(value)
            "st" //stone
            -> Height = UnitConverter.convert_from_stone_to_kilograms(value)
        }
    }

    //calculate bmi
    fun calculateBMI(): Float {

        return if (Height != 0f && Weight != 0f) {

            //calulate bmi (kg/m^2)
            Weight / (Height * Height)
        } else {
            0f
        }

    }
}