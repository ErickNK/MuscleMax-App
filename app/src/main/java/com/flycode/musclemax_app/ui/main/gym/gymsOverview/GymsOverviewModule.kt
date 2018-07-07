package com.flycode.musclemax_app.ui.main.gym.gymsOverview

import android.arch.lifecycle.ViewModelProviders
import com.flycode.timespace.di.scope.PerFragmentLevel1
import dagger.Module
import dagger.Provides

@Module
class GymsOverviewModule {
    @Provides
    @PerFragmentLevel1
    fun providePresenter(

    ): GymsOverviewPresenter = GymsOverviewPresenter(

    )

    @Provides
    @PerFragmentLevel1
    fun provideViewModel(
            GymsOverviewFragment: GymsOverviewFragment,
            GymsOverviewPresenter: GymsOverviewPresenter
    ): GymsOverviewViewModel {
        val viewModel = ViewModelProviders.of(GymsOverviewFragment).get(GymsOverviewViewModel::class.java)
        GymsOverviewPresenter.viewModel = viewModel
        viewModel.presenter = GymsOverviewPresenter
        return viewModel
    }
}