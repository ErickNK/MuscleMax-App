package com.flycode.musclemax_app.di.module

import com.flycode.musclemax_app.service.nearbyGymService.NearbyGymModule
import com.flycode.musclemax_app.service.nearbyGymService.NearbyGymService
import com.flycode.musclemax_app.ui.auth.AuthActivity
import com.flycode.musclemax_app.ui.main.MainActivity
import com.flycode.musclemax_app.ui.main.MainModule
import com.flycode.musclemax_app.ui.splash.SplashActivity
import com.flycode.musclemax_app.ui.splash.SplashModule
import com.flycode.timespace.di.scope.PerActivity
import com.flycode.timespace.di.scope.PerService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BindingsModule {
    @PerActivity
    @ContributesAndroidInjector(modules = [(SplashModule::class)])
    internal abstract fun splashActivity(): SplashActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [(com.flycode.musclemax_app.ui.auth.FragmentProvider::class)])
    internal abstract fun authActivity(): AuthActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [(MainModule::class),(com.flycode.musclemax_app.ui.main.FragmentProvider::class)])
    internal abstract fun mainActivity(): MainActivity

    @PerService
    @ContributesAndroidInjector(modules = [(NearbyGymModule::class)])
    internal abstract fun nearbyGymService(): NearbyGymService
}
