package com.flycode.musclemax_app.ui.main.userprofile

import android.arch.lifecycle.ViewModelProviders
import com.flycode.timespace.di.scope.PerFragmentLevel1
import dagger.Module
import dagger.Provides

@Module
class UserProfileModule {
    @Provides
    @PerFragmentLevel1
    fun providePresenter(

    ): UserProfilePresenter = UserProfilePresenter(

    )

    @Provides
    @PerFragmentLevel1
    fun provideViewModel(
            UserProfileFragment: UserProfileFragment,
            UserProfilePresenter: UserProfilePresenter
    ): UserProfileViewModel {
        val viewModel = ViewModelProviders.of(UserProfileFragment).get(UserProfileViewModel::class.java)
        UserProfilePresenter.viewModel = viewModel
        viewModel.presenter = UserProfilePresenter
        return viewModel
    }
}