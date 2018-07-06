package com.flycode.musclemax_app.service.nearbyGymService

import android.content.SharedPreferences
import com.apollographql.apollo.ApolloClient
import com.flycode.musclemax_app.data.network.GoogleMapsService
import com.flycode.musclemax_app.trackers.DistanceToDestinationTracker
import com.flycode.musclemax_app.trackers.GpsTracker
import com.flycode.timespace.di.scope.PerService
import com.pusher.client.Pusher
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class NearbyGymModule{

    @Provides
    @PerService
    fun providePresenter(
            apolloClient: ApolloClient,
            gpsTracker: GpsTracker
    ): NearbyGymPresenter<NearbyGymService>
            = NearbyGymPresenter(
            apolloClient = apolloClient,
            gpsTracker = gpsTracker
    )

    @Provides
    @PerService
    fun provideCompositeDisposable(
            nearbyGymPresenter: NearbyGymPresenter<NearbyGymService>
    ) = nearbyGymPresenter.compositeDisposable

    @Provides
    @PerService
    fun provideDistanceToDestinationTracker(
            nearbyGymService: NearbyGymService,
            googleMapsService: GoogleMapsService,
            compositeDisposable: CompositeDisposable
    )= DistanceToDestinationTracker(
            context = nearbyGymService,
            googleMapsService = googleMapsService,
            compositeDisposable = compositeDisposable
    ).apply {
        nearbyGymService.lifecycle.addObserver(this)
        listener = nearbyGymService
    }

    @Provides
    @PerService
    fun provideGPSTracker(
            nearbyGymService: NearbyGymService
    )= GpsTracker(
            context = nearbyGymService
    ).apply {
        nearbyGymService.lifecycle.addObserver(this)
        listener = nearbyGymService
    }
}