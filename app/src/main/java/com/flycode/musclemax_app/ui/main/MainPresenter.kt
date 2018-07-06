package com.flycode.musclemax_app.ui.main

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.content.SharedPreferences
import com.flycode.musclemax_app.R
import com.flycode.musclemax_app.data.network.AuthService
import com.flycode.musclemax_app.ui.auth.AuthActivity
import com.flycode.musclemax_app.ui.base.BasePresenter
import com.raizlabs.android.dbflow.kotlinextensions.delete
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainPresenter(
        val sharedPreferences: SharedPreferences,
        val authService: AuthService
) : BasePresenter<MainActivity, MainPresenter, MainViewModel>()
        , MainContract.MainPresenter<MainActivity> {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun init(){
        viewModel.defaultUser = utilityWrapper.defaultUser
        if(!utilityWrapper.defaultUser.pictures.isEmpty()
                && utilityWrapper.defaultUser.pictures[0].local_location.isEmpty() ){
            view?.loadUserProfilePicture(utilityWrapper.defaultUser.pictures[0].local_location)
        }
    }
    override fun logout() {
        view?.let{view ->
            viewModel.uiState.isLoading = true
            compositeDisposable.add(
                    authService
                    .logout()
                    .subscribeOn(Schedulers.io())
                    .flatMap{
//                        sharedPreferences.edit()
                        Observable.just(utilityWrapper.defaultUser.delete())
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe ({
                        if(it){
                            view.navigateToActivity(view, AuthActivity::class.java)
                        }else{
                            view.showError(view.resources.getString(R.string.something_went_wrong))
                        }
                        viewModel.uiState.isLoading = false
                    },{
                        viewModel.uiState.isLoading = false
                        viewModel.uiState.onError = false
                        if (it.message != null){
                            view.showError(message = it.message.toString())
                        }else{
                            view.showError(view.resources.getString(R.string.something_went_wrong))
                        }
                    })
            )
        }
    }

}