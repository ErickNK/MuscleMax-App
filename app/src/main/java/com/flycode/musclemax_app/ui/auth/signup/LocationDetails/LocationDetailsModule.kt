package com.flycode.musclemax_app.ui.auth.signup.LocationDetails

import android.arch.lifecycle.ViewModelProviders
import com.flycode.musclemax_app.trackers.GpsTracker
import com.flycode.musclemax_app.ui.auth.signup.SignUpViewModel
import com.flycode.timespace.di.scope.PerFragmentLevel2
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.PlaceDetectionClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import dagger.Module
import dagger.Provides

@Module
class LocationDetailsModule {
    @Provides
    @PerFragmentLevel2
    fun providePresenter(
            superViewModel: SignUpViewModel,
            placeAutocompleteAdapter: PlaceAutocompleteAdapter
    ): LocationDetailsPresenter
            = LocationDetailsPresenter(
            superViewModel = superViewModel,
            placeAutocompleteAdapter = placeAutocompleteAdapter
    )

    @Provides
    @PerFragmentLevel2
    fun provideViewModel(
            LocationDetailsFragment: LocationDetailsFragment,
            LocationDetailsPresenter: LocationDetailsPresenter
    ) : LocationDetailsViewModel{
        val viewModel = ViewModelProviders.of(LocationDetailsFragment).get(LocationDetailsViewModel::class.java)
        LocationDetailsPresenter.viewModel = viewModel
        viewModel.presenter = LocationDetailsPresenter
        return viewModel
    }

    @Provides
    @PerFragmentLevel2
    fun provideGeoDataClient(
            locationDetailsFragment: LocationDetailsFragment
    ): GeoDataClient = Places.getGeoDataClient(locationDetailsFragment.activity!!, null)

    @Provides
    @PerFragmentLevel2
    fun providePlaceDetectionClient(
            locationDetailsFragment: LocationDetailsFragment
    ): PlaceDetectionClient = Places.getPlaceDetectionClient(locationDetailsFragment.activity!!, null)


    @Provides
    @PerFragmentLevel2
    fun provideAutoCompleteAdapter(
            LocationDetailsFragment: LocationDetailsFragment,
            geoDataClient: GeoDataClient
    )= PlaceAutocompleteAdapter(
            context = LocationDetailsFragment.context!!,
            mGeoDataClient = geoDataClient,
            mBounds = LatLngBounds(LatLng((-40).toDouble(), (-168).toDouble()),LatLng(71.0, 136.0)),
            mPlaceFilter = AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE).build()
    )

    @Provides
    @PerFragmentLevel2
    fun provideGPSTracker(
            LocationDetailsFragment: LocationDetailsFragment
    )= GpsTracker(
            context = LocationDetailsFragment.context!!
    ).apply {
        LocationDetailsFragment.lifecycle.addObserver(this)
        listener = LocationDetailsFragment
    }
}