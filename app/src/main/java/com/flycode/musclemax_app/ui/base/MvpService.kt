package com.flycode.musclemax_app.ui.base

import android.os.Bundle

interface MvpService {

    fun sendSuccess(message: String)

    fun sendError(error: String)

    fun sendOnFinish(success: Boolean, data: Bundle)

    fun stopSelf()

}
