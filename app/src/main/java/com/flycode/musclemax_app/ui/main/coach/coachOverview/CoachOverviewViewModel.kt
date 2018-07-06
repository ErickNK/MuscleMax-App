package com.flycode.musclemax_app.ui.main.coach.coachOverview

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.musclemax_app.data.models.User
import com.flycode.musclemax_app.ui.base.BaseViewModel
import com.flycode.musclemax_app.ui.flexibleItems.CoachesHeaderItem

class CoachOverviewViewModel
    : BaseViewModel<CoachOverviewFragment, CoachOverviewPresenter>() {
    var coaches: MutableList<User> = ArrayList()
    var lastTextEdit: Long = 0

    var headingsList: MutableList<CoachesHeaderItem> = ArrayList()

    val uiState = UiState()
    lateinit var favouritesHeader: CoachesHeaderItem
    lateinit var allCoachesHeader: CoachesHeaderItem

    class UiState : BaseObservable(){

        @get: Bindable
        var isLoading: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var onError: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

    }
}