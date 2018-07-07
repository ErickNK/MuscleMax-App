package com.flycode.musclemax_app.ui.main

import com.flycode.musclemax_app.ui.base.BaseContract

class MainContract {
    interface MainActivity : BaseContract.View
    interface MainPresenter<V : MainActivity> : BaseContract.Presenter<V>{
        fun logout()
    }
}