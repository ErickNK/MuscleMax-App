package com.flycode.musclemax_app.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.content.SharedPreferences
import com.flycode.musclemax_app.data.network.AuthService
import dagger.Module
import dagger.Provides

@Module
class MainModule{

    @Provides
    fun providePresenter(
            sharedPreferences: SharedPreferences,
            authService: AuthService
    ): MainPresenter {
        return MainPresenter(
                sharedPreferences = sharedPreferences,
                authService = authService
        )
    }

    @Provides
    fun provideViewModel(
            MainActivity : MainActivity,
            MainPresenter: MainPresenter
    ): MainViewModel {
        val viewModel = ViewModelProviders.of(MainActivity).get(MainViewModel::class.java)
        MainPresenter.viewModel = viewModel
        viewModel.presenter = MainPresenter
        return viewModel
    }
}
