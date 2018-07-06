package com.flycode.musclemax_app.ui.main.userprofile

import com.flycode.musclemax_app.data.models.Location
import com.flycode.musclemax_app.data.models.User
import com.flycode.musclemax_app.data.models.WeightMeasurement
import com.flycode.musclemax_app.ui.base.BaseViewModel

class UserProfileViewModel
    : BaseViewModel<UserProfileFragment, UserProfilePresenter>() {
    var defaultUser: User = User()
    val weightMeasurement : WeightMeasurement = WeightMeasurement()
    val location : Location = Location()

}