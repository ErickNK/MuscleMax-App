package com.flycode.musclemax_app.ui.main

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.musclemax_app.data.models.User
import com.flycode.musclemax_app.ui.base.BaseViewModel

class MainViewModel
    : BaseViewModel<MainActivity, MainPresenter>(){
    var defaultUser = User()
    val uiState = UiState()
    class UiState : BaseObservable(){
        @get: Bindable
        var isLoading: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var onError: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

    }
}