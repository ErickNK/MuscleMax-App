package com.flycode.musclemax_app.ui.main.map

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.musclemax_app.data.models.Gym
import com.flycode.musclemax_app.ui.base.BaseViewModel
import com.flycode.musclemax_app.ui.flexibleItems.SearchResultsHeaderItem

class MapViewModel
    : BaseViewModel<MapFragment, MapPresenter>() {
    var gyms: MutableList<Gym> = ArrayList()
    var nearbyDistance: Int = 200
    var lastTextEdit: Long = 0

    val uiState = UiState()

    var searchResultHeaderItem: SearchResultsHeaderItem = SearchResultsHeaderItem(resultCount = 0)
    var gymSearchResultsList: MutableList<Gym> = ArrayList()


    class UiState : BaseObservable(){
        @get: Bindable
        var isBottomSheetPicLoading: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var isHintOpen: Boolean = true
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var bottomSheetState: Int = 0
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var isAppBarHidden: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

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

        @get: Bindable
        var isBottomSheetToolbarHidden: Boolean = true
            set(value) {
                field = value
                notifyChange()
            }
    }
}