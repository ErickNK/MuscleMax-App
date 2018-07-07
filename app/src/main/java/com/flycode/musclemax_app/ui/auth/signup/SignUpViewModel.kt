package com.flycode.musclemax_app.ui.auth.signup

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.graphics.Bitmap
import com.flycode.musclemax_app.data.models.Location
import com.flycode.musclemax_app.data.models.User
import com.flycode.musclemax_app.data.models.WeightMeasurement
import com.flycode.musclemax_app.ui.base.BaseViewModel

class SignUpViewModel
    : BaseViewModel<SignUpFragment, SignUpPresenter>(){
    var user : User = User()
    val weightMeasurement : WeightMeasurement = WeightMeasurement()
    val location : Location = Location()
    var imagePath: String? = null
    var imageBitmap: Bitmap? = null
    var doImageSave: Boolean = false
    var signedUp: Boolean = false
    val uiState = UiState()

    var weightUnits: String = "Kg(s)"
    var heightUnits: String = "m(s)"

    var doSaveLocation: Boolean = true

    class UiState : BaseObservable(){
        @get: Bindable
        var googleLoginSuccess: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var facebookLoginSuccess: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

    }
}
