package com.flycode.musclemax_app.ui.auth.signin

import android.arch.lifecycle.ViewModelProviders
import android.content.SharedPreferences
import com.flycode.musclemax_app.data.network.AuthService
import com.flycode.timespace.di.scope.PerFragment
import dagger.Module
import dagger.Provides

@Module
class SignInModule {

    @Provides
    @PerFragment
    fun providePresenter(
            sharedPreferences: SharedPreferences,
            authService: AuthService
    ): SignInPresenter
        = SignInPresenter(
            authService = authService,
            sharedPreferences = sharedPreferences
        )

    @Provides
    @PerFragment
    fun provideViewModel(
            signInFragment: SignInFragment,
            signInPresenter: SignInPresenter
    ) : SignInViewModel{
        val viewModel = ViewModelProviders.of(signInFragment).get(SignInViewModel::class.java)
        signInPresenter.viewModel = viewModel
        viewModel.presenter = signInPresenter
        return viewModel
    }

}