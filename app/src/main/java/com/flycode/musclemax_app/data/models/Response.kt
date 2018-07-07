package com.flycode.musclemax_app.data.models

import java.io.Serializable

data class Response< D > (
        var status: String,
        var message: String,
        var data : D
):Serializable