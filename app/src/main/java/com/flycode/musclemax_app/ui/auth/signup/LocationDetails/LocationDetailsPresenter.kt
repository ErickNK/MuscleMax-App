package com.flycode.musclemax_app.ui.auth.signup.LocationDetails

import android.view.View
import android.widget.AdapterView
import com.flycode.musclemax_app.R
import com.flycode.musclemax_app.ui.auth.signup.SignUpViewModel
import com.flycode.musclemax_app.ui.base.BasePresenter
import com.google.android.gms.location.places.Places

class LocationDetailsPresenter(
        val placeAutocompleteAdapter: PlaceAutocompleteAdapter,
        val superViewModel: SignUpViewModel
)
    : BasePresenter<LocationDetailsFragment, LocationDetailsPresenter, LocationDetailsViewModel>(),
        LocationDetailsContract.LocationDetailsPresenter<LocationDetailsFragment>{

    fun onSuggestionItemPicked(adapterView: AdapterView<*>, view1: View, i: Int, l: Long) {
        view?.let { view ->
            Places.GeoDataApi.getPlaceById(
                    view.googleApiClient!!,
                    placeAutocompleteAdapter.getItem(i)?.placeId
            ).setResultCallback {
                if (!it.status.isSuccess) {
                    view.showError(view.resources.getString(R.string.something_went_wrong)!!)
                    it.release()
                }

                superViewModel.location.apply {
                    this.address = it[0].address.toString()
                    this.latLng = "${it[0].latLng.latitude},${it[0].latLng.longitude}"
                }
                view.moveCamera(it[0].latLng,12f,true)
                superViewModel.doSaveLocation = true

                it.release()
            }
        }
    }
}