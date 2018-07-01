package com.flycode.musclemax_app.ui.auth

import com.flycode.musclemax_app.ui.auth.landingPage.AuthLandingPageFragment
import com.flycode.musclemax_app.ui.auth.pincode.PinCodeFragment
import com.flycode.musclemax_app.ui.auth.signin.SignInFragment
import com.flycode.musclemax_app.ui.auth.signin.SignInModule
import com.flycode.musclemax_app.ui.auth.signup.SignUpFragment
import com.flycode.musclemax_app.ui.auth.signup.SignUpModule
import com.flycode.timespace.di.scope.PerFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentProvider {

    @PerFragment
    @ContributesAndroidInjector()
    internal abstract fun authLandingPageFragment(): AuthLandingPageFragment

    @PerFragment
    @ContributesAndroidInjector()
    internal abstract fun pinCodeFragment(): PinCodeFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [(SignInModule::class)])
    internal abstract fun signInFragment(): SignInFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [(SignUpModule::class)])
    internal abstract fun signUpFragment(): SignUpFragment
}