package com.flycode.musclemax_app.ui.auth.signup.LocationDetails

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.location.Address
import com.flycode.musclemax_app.ui.base.BaseViewModel

class LocationDetailsViewModel
    : BaseViewModel<LocationDetailsFragment, LocationDetailsPresenter>() {

    var searchLocationResults: MutableList<Address> = ArrayList()
    var lastTextEdit: Long = 0
    val uiState = UiState()
    class UiState : BaseObservable(){

        @get: Bindable
        var onSearchError: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var isSearchLoading: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var onMapError: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var isMapLoading: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var isSearchOpen: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }
    }
}