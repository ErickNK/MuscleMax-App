package com.flycode.musclemax_app.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginPayload(
    @field: SerializedName("token")
    var token : String,

    @field: SerializedName("user")
    var user : User
): Serializable