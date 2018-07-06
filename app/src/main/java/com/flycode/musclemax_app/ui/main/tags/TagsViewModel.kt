package com.flycode.musclemax_app.ui.main.tags

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.musclemax_app.ui.base.BaseViewModel
import com.flycode.musclemax_app.ui.main.map.MapViewModel

class TagsViewModel: BaseViewModel<TagsFragment, TagsPresenter>() {
    val uiState = UiState()

    class UiState : BaseObservable(){
        @get: Bindable
        var onError: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var isLoading: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }
    }
}