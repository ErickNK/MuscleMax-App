package com.flycode.musclemax_app.ui.splash

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.os.CountDownTimer
import com.flycode.musclemax_app.ui.auth.AuthActivity
import com.flycode.musclemax_app.ui.base.BasePresenter
import com.flycode.musclemax_app.ui.main.MainActivity

//import com.flycode.musclemax_app.ui.main.MainActivity

class SplashPresenter
    : BasePresenter<SplashActivity,SplashPresenter,SplashViewModel>()
        , SplashContract.SplashPresenter<SplashActivity> {

    val WAITING_TIME: Long = 1000

    @OnLifecycleEvent(value = Lifecycle.Event.ON_START)
    override fun startCounting() {
        object : CountDownTimer(WAITING_TIME, 1000) {

            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                /**
                 * If no registered user send to authentication activity
                 * */
                if (isUserRegistered()) //Check if user registered
                    view?.navigateToActivity(to = MainActivity::class.java, from = null)
                else
                    view?.navigateToActivity(to = AuthActivity::class.java, from = null)
            }
        }.start()
    }

    /**
     * Check if the app has a registered user.
     *
     */
    private fun isUserRegistered(): Boolean {
        return utilityWrapper.defaultUser.id > 0
    }
}