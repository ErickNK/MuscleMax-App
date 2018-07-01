package com.flycode.musclemax_app.ui.auth.signup

import android.arch.lifecycle.ViewModelProviders
import com.flycode.musclemax_app.data.network.TempService
import com.flycode.musclemax_app.data.network.UserService
import com.flycode.timespace.di.scope.PerFragment
import dagger.Module
import dagger.Provides

@Module
class SignUpModule {
    @Provides
    @PerFragment
    fun providePresenter(
            userService: UserService,
            tempService: TempService
    ): SignUpPresenter
        = SignUpPresenter(
            userService = userService,
            tempService = tempService
        )

    @Provides
    @PerFragment
    fun provideViewModel(
            signUpFragment: SignUpFragment,
            signUpPresenter: SignUpPresenter
    ) : SignUpViewModel{
        val viewModel = ViewModelProviders.of(signUpFragment).get(SignUpViewModel::class.java)
        signUpPresenter.viewModel = viewModel
        viewModel.presenter = signUpPresenter
        return viewModel
    }
}