package com.flycode.musclemax_app.ui.main

import com.flycode.musclemax_app.ui.main.map.MapFragment
import com.flycode.timespace.di.scope.PerFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentProvider {

    @PerFragment
    @ContributesAndroidInjector()
    internal abstract fun mapFragment(): MapFragment

}