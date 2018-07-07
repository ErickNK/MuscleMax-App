package com.flycode.musclemax_app.ui.auth.signup.PersonalDetails

import android.arch.lifecycle.ViewModelProviders
import com.flycode.musclemax_app.ui.auth.signup.SignUpViewModel
import com.flycode.timespace.di.scope.PerFragmentLevel2
import dagger.Module
import dagger.Provides

@Module
class PersonalDetailsModule {
    @Provides
    @PerFragmentLevel2
    fun providePresenter(
            superViewModel: SignUpViewModel
    ): PersonalDetailsPresenter
            = PersonalDetailsPresenter(
            superViewModel = superViewModel
    )

    @Provides
    @PerFragmentLevel2
    fun provideViewModel(
            PersonalDetailsFragment: PersonalDetailsFragment,
            PersonalDetailsPresenter: PersonalDetailsPresenter
    ) : PersonalDetailsViewModel{
        val viewModel = ViewModelProviders.of(PersonalDetailsFragment).get(PersonalDetailsViewModel::class.java)
        PersonalDetailsPresenter.viewModel = viewModel
        viewModel.presenter = PersonalDetailsPresenter
        return viewModel
    }
}