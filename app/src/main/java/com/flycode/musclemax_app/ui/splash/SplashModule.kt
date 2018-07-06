package com.flycode.musclemax_app.ui.splash

import android.arch.lifecycle.ViewModelProviders
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides

@Module
class SplashModule{

    @Provides
    fun providePresenter(
            sharedPreferences: SharedPreferences
    ): SplashPresenter{
        return SplashPresenter(
                sharedPreferences = sharedPreferences
        )
    }

    @Provides
    fun provideViewModel(
            splashActivity : SplashActivity,
            splashPresenter: SplashPresenter
    ): SplashViewModel{
        val viewModel = ViewModelProviders.of(splashActivity).get(SplashViewModel::class.java)
        splashPresenter.viewModel = viewModel
        viewModel.presenter = splashPresenter
        return viewModel
    }
}
