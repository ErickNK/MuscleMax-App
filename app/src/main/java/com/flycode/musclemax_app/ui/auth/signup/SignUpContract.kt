package com.flycode.musclemax_app.ui.auth.signup

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import com.flycode.musclemax_app.ui.base.BaseContract

interface SignUpContract {
    interface SignUpFragment : BaseContract.View {
        fun onSignIn()
        fun onFinish()
    }

    interface SignUpPresenter<V : SignUpFragment> : BaseContract.Presenter<V>{
        fun onFinish()
    }
}