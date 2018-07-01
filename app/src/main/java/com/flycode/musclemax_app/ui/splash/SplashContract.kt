package com.flycode.musclemax_app.ui.splash

import com.flycode.musclemax_app.ui.base.BaseContract

interface SplashContract : BaseContract{
    interface SplashActivity : BaseContract.View
    interface SplashPresenter<V : SplashActivity> : BaseContract.Presenter<V>{
        fun startCounting()
    }
    interface SplashViewModel<V : SplashActivity, P : SplashPresenter<V>> : BaseContract.ViewModel< V,P>
}