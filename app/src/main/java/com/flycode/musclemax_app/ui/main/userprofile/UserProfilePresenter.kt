package com.flycode.musclemax_app.ui.main.userprofile

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import com.flycode.musclemax_app.ui.base.BasePresenter

class UserProfilePresenter
    : BasePresenter<UserProfileFragment, UserProfilePresenter, UserProfileViewModel>(),
        UserProfileContract.UserProfilePresenter<UserProfileFragment> {


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun init(){
        viewModel.defaultUser = utilityWrapper.defaultUser
        if(!utilityWrapper.defaultUser.pictures.isEmpty()
                && utilityWrapper.defaultUser.pictures[0].local_location.isEmpty() ){
            view?.loadUserProfilePicture(utilityWrapper.defaultUser.pictures[0].local_location)
        }
    }
}