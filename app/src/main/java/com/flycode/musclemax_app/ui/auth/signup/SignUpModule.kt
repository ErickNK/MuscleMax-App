package com.flycode.musclemax_app.ui.auth.signup

import android.arch.lifecycle.ViewModelProviders
import android.content.SharedPreferences
import com.apollographql.apollo.ApolloClient
import com.flycode.musclemax_app.data.network.TempService
import com.flycode.timespace.di.scope.PerFragmentLevel1
import dagger.Module
import dagger.Provides

@Module
class SignUpModule {
    @Provides
    @PerFragmentLevel1
    fun providePresenter(
            apolloClient: ApolloClient,
            tempService: TempService,
            sharedPreferences: SharedPreferences
    ): SignUpPresenter
        = SignUpPresenter(
             apolloClient = apolloClient,
            tempService = tempService,
            sharedPreferences = sharedPreferences
        )

    @Provides
    @PerFragmentLevel1
    fun provideViewModel(
            signUpFragment: SignUpFragment,
            signUpPresenter: SignUpPresenter
    ) : SignUpViewModel{
        val viewModel = ViewModelProviders.of(signUpFragment).get(SignUpViewModel::class.java)
        signUpPresenter.viewModel = viewModel
        viewModel.presenter = signUpPresenter
        return viewModel
    }

    @Provides
    @PerFragmentLevel1
    fun provideViewPagerAdapter(
           signUpFragment: SignUpFragment
    ) : ViewPagerAdapter = ViewPagerAdapter(signUpFragment.fragmentManager!!)
}