package com.flycode.musclemax_app.ui.auth.signup

import com.flycode.musclemax_app.ui.auth.signup.DetailsOverview.DetailsOverviewFragment
import com.flycode.musclemax_app.ui.auth.signup.HeightDetails.HeightDetailsFragment
import com.flycode.musclemax_app.ui.auth.signup.LocationDetails.LocationDetailsFragment
import com.flycode.musclemax_app.ui.auth.signup.LocationDetails.LocationDetailsModule
import com.flycode.musclemax_app.ui.auth.signup.PersonalDetails.PersonalDetailsFragment
import com.flycode.musclemax_app.ui.auth.signup.PersonalDetails.PersonalDetailsModule
import com.flycode.musclemax_app.ui.auth.signup.WeightDetails.WeightDetailsFragment
import com.flycode.timespace.di.scope.PerFragmentLevel2

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentProvider {

    @PerFragmentLevel2
    @ContributesAndroidInjector(modules = [(PersonalDetailsModule::class)])
    internal abstract fun personalDetailsFragment(): PersonalDetailsFragment

    @PerFragmentLevel2
    @ContributesAndroidInjector(modules = [(LocationDetailsModule::class)])
    internal abstract fun locationDetailsFragment(): LocationDetailsFragment

    @PerFragmentLevel2
    @ContributesAndroidInjector()
    internal abstract fun weightDetailsFragment(): WeightDetailsFragment

    @PerFragmentLevel2
    @ContributesAndroidInjector()
    internal abstract fun heightDetailsFragment(): HeightDetailsFragment

    @PerFragmentLevel2
    @ContributesAndroidInjector()
    internal abstract fun detailsOverviewFragment(): DetailsOverviewFragment
}