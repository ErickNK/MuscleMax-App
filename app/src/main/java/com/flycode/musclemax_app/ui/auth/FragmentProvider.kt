package com.flycode.musclemax_app.ui.auth

import com.flycode.musclemax_app.ui.auth.landingPage.AuthLandingPageFragment
import com.flycode.musclemax_app.ui.auth.landingPage.AuthLandingPageModule
import com.flycode.musclemax_app.ui.auth.signin.SignInFragment
import com.flycode.musclemax_app.ui.auth.signin.SignInModule
import com.flycode.musclemax_app.ui.auth.signup.FragmentProvider
import com.flycode.musclemax_app.ui.auth.signup.SignUpFragment
import com.flycode.musclemax_app.ui.auth.signup.SignUpModule
import com.flycode.timespace.di.scope.PerFragmentLevel1
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentProvider {

    @PerFragmentLevel1
    @ContributesAndroidInjector(modules = [(AuthLandingPageModule::class)])
    internal abstract fun authLandingPageFragment(): AuthLandingPageFragment

    @PerFragmentLevel1
    @ContributesAndroidInjector(modules = [(SignInModule::class)])
    internal abstract fun signInFragment(): SignInFragment

    @PerFragmentLevel1
    @ContributesAndroidInjector(modules = [(SignUpModule::class),(FragmentProvider::class)])
    internal abstract fun signUpFragment(): SignUpFragment
}