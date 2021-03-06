package com.flycode.musclemax_app.ui.main.gym.gymView

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.musclemax_app.data.models.Gym
import com.flycode.musclemax_app.ui.base.BaseViewModel

class GymViewViewModel
    : BaseViewModel<GymViewFragment, GymViewPresenter>()  {
    var gym: Gym = Gym()
    val uiState = UiState()

    class UiState : BaseObservable(){
        @get: Bindable
        var isEmptyReviewsHidden: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var isEmptyTagsHidden: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var isJoined: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var isPending: Boolean = false
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

        @get: Bindable
        var onError: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var isTagsLoading: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var onTagsError: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var isOtherDetailsHidden: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        @get: Bindable
        var isReviewsHidden: Boolean = false
            set(value) {
                field = value
                notifyChange()
            }

        fun getJoinedState(): String{
            return when {
                isJoined -> "JOINED"
                isPending -> "PENDING"
                else -> "JOIN"
            }
        }

        fun getGymLocation():String{
            return "SomeLocation"
        }
    }
}