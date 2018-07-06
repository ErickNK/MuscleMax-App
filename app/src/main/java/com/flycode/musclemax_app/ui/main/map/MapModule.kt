package com.flycode.musclemax_app.ui.main.map

import android.arch.lifecycle.ViewModelProviders
import com.apollographql.apollo.ApolloClient
import com.flycode.musclemax_app.ui.flexibleItems.SearchResultsHeaderItem
import com.flycode.timespace.di.scope.PerFragmentLevel1
import dagger.Module
import dagger.Provides
import eu.davidea.flexibleadapter.FlexibleAdapter

@Module
class MapModule {
    @Provides
    @PerFragmentLevel1
    fun providePresenter(
            mapsAdapter: MapsAdapter,
            apolloClient: ApolloClient,
            searchListAdapter: FlexibleAdapter<SearchResultsHeaderItem>
    ): MapPresenter = MapPresenter(
            mapsAdapter = mapsAdapter,
            apolloClient = apolloClient,
            searchListAdapter = searchListAdapter
    )

    @Provides
    @PerFragmentLevel1
    fun provideViewModel(
            mapFragment: MapFragment,
            mapPresenter: MapPresenter
    ) : MapViewModel {
        val viewModel = ViewModelProviders.of(mapFragment).get(MapViewModel::class.java)
        mapPresenter.viewModel = viewModel
        viewModel.presenter = mapPresenter
        return viewModel
    }

    @Provides
    @PerFragmentLevel1
    fun provideMapsAdapter(
            mapFragment: MapFragment
    ): MapsAdapter = MapsAdapter(
            context = mapFragment.context!!
    )

    @Provides
    @PerFragmentLevel1
    fun provideGymImagesPager(
            mapFragment: MapFragment
    ): GymImagesPager = GymImagesPager(
            context = mapFragment.context!!
    )

    @Provides
    @PerFragmentLevel1
    fun provideSearchListAdapter(): FlexibleAdapter<SearchResultsHeaderItem> = FlexibleAdapter(null)
}