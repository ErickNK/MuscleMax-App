package com.flycode.musclemax_app.ui.main.coach.coachOverview

import android.arch.lifecycle.ViewModelProviders
import com.apollographql.apollo.ApolloClient
import com.flycode.musclemax_app.ui.flexibleItems.CoachesHeaderItem
import com.flycode.timespace.di.scope.PerFragmentLevel1
import dagger.Module
import dagger.Provides
import eu.davidea.flexibleadapter.FlexibleAdapter

@Module
class CoachOverviewModule {
    @Provides
    @PerFragmentLevel1
    fun providePresenter(
            apolloClient: ApolloClient,
            mainListAdapter: FlexibleAdapter<CoachesHeaderItem>
    ): CoachOverviewPresenter = CoachOverviewPresenter(
            apolloClient = apolloClient,
            mainListAdapter = mainListAdapter
    )

    @Provides
    @PerFragmentLevel1
    fun provideViewModel(
            CoachOverviewFragment: CoachOverviewFragment,
            CoachOverviewPresenter: CoachOverviewPresenter
    ): CoachOverviewViewModel {
        val viewModel = ViewModelProviders.of(CoachOverviewFragment).get(CoachOverviewViewModel::class.java)
        CoachOverviewPresenter.viewModel = viewModel
        viewModel.presenter = CoachOverviewPresenter
        return viewModel
    }

    @Provides
    @PerFragmentLevel1
    fun provideCoachesListAdapter(): FlexibleAdapter<CoachesHeaderItem> = FlexibleAdapter(null)
}