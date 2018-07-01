package com.flycode.musclemax_app.ui.auth.signup

import android.graphics.Bitmap
import com.flycode.musclemax_app.R
import com.flycode.musclemax_app.data.models.User
import com.flycode.musclemax_app.ui.base.BaseViewModel

class SignUpViewModel
    : BaseViewModel<SignUpFragment, SignUpPresenter>(){
    val user : User = User()
    var profilePic = R.drawable.image_placeholder
    var profilePicBitmap : Bitmap? = null
}
