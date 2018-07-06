package com.flycode.musclemax_app.ui.auth.landingPage

import android.arch.lifecycle.ViewModelProviders
import android.content.SharedPreferences
import com.flycode.musclemax_app.data.network.AuthService
import com.flycode.musclemax_app.ui.auth.signup.SignUpViewModel
import com.flycode.timespace.di.scope.PerFragmentLevel1
import com.flycode.timespace.di.scope.PerFragmentLevel2
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.Module
import dagger.Provides

@Module
open class AuthLandingPageModule {

    @Provides
    @PerFragmentLevel1
    fun providePresenter(
            authService: AuthService,
            googleSignInClient: GoogleSignInClient,
            sharedPreferences: SharedPreferences
    ): AuthLandingPagePresenter
            = AuthLandingPagePresenter(
            authService = authService,
            googleSignInClient = googleSignInClient,
            sharedPreferences = sharedPreferences
    )

    @Provides
    @PerFragmentLevel1
    fun provideViewModel(
            AuthLandingPageFragment: AuthLandingPageFragment,
            AuthLandingPagePresenter: AuthLandingPagePresenter
    ) : AuthLandingPageViewModel{
        val viewModel = ViewModelProviders.of(AuthLandingPageFragment).get(AuthLandingPageViewModel::class.java)
        AuthLandingPagePresenter.viewModel = viewModel
        viewModel.presenter = AuthLandingPagePresenter
        return viewModel
    }
}