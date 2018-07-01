package com.flycode.musclemax_app.ui.base

import com.flycode.musclemax_app.di.component.DaggerPresenterComponent


open class BaseServicePresenter<V : MvpService>(val service: V){
    private val utilityWrapper = UtilityWrapper()

    init {
        DaggerPresenterComponent.create().inject(utilityWrapper)
    }
}
