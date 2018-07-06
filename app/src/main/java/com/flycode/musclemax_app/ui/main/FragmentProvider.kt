package com.flycode.musclemax_app.ui.main

import com.flycode.musclemax_app.ui.main.coach.coachOverview.CoachOverviewFragment
import com.flycode.musclemax_app.ui.main.coach.coachOverview.CoachOverviewModule
import com.flycode.musclemax_app.ui.main.coach.coachView.CoachViewFragment
import com.flycode.musclemax_app.ui.main.coach.coachView.CoachViewModule
import com.flycode.musclemax_app.ui.main.gym.gymView.GymViewFragment
import com.flycode.musclemax_app.ui.main.gym.gymView.GymViewModule
import com.flycode.musclemax_app.ui.main.gym.gymsOverview.GymsOverviewFragment
import com.flycode.musclemax_app.ui.main.gym.gymsOverview.GymsOverviewModule
import com.flycode.musclemax_app.ui.main.map.MapFragment
import com.flycode.musclemax_app.ui.main.map.MapModule
import com.flycode.musclemax_app.ui.main.tags.TagsFragment
import com.flycode.musclemax_app.ui.main.tags.TagsModule
import com.flycode.musclemax_app.ui.main.userprofile.UserProfileFragment
import com.flycode.musclemax_app.ui.main.userprofile.UserProfileModule
import com.flycode.timespace.di.scope.PerFragmentLevel1
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentProvider {

    @PerFragmentLevel1
    @ContributesAndroidInjector(modules = [(MapModule::class)])
    internal abstract fun mapFragment(): MapFragment

    @PerFragmentLevel1
    @ContributesAndroidInjector(modules = [(TagsModule::class)])
    internal abstract fun tagsFragment(): TagsFragment

    @PerFragmentLevel1
    @ContributesAndroidInjector(modules = [(GymViewModule::class)])
    internal abstract fun gymViewFragment(): GymViewFragment

    @PerFragmentLevel1
    @ContributesAndroidInjector(modules = [(CoachViewModule::class)])
    internal abstract fun coachViewFragment(): CoachViewFragment

    @PerFragmentLevel1
    @ContributesAndroidInjector(modules = [(CoachOverviewModule::class)])
    internal abstract fun coachOverviewFragment(): CoachOverviewFragment

    @PerFragmentLevel1
    @ContributesAndroidInjector(modules = [(GymsOverviewModule::class)])
    internal abstract fun gymsOverviewFragment(): GymsOverviewFragment

    @PerFragmentLevel1
    @ContributesAndroidInjector(modules = [(UserProfileModule::class)])
    internal abstract fun userProfileFragment(): UserProfileFragment

}