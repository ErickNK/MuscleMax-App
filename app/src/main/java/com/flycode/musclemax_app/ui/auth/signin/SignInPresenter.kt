package com.flycode.musclemax_app.ui.auth.signin

import android.content.SharedPreferences
import com.flycode.musclemax_app.data.network.AuthService
import com.flycode.musclemax_app.ui.base.BasePresenter
import com.raizlabs.android.dbflow.kotlinextensions.save
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SignInPresenter(
        val authService: AuthService,
        val sharedPreferences: SharedPreferences
) : BasePresenter<SignInFragment,SignInPresenter,SignInViewModel>(),
        SignInContract.SignInPresenter<SignInFragment>{

    override fun login(){
        view?.let { view ->
            authService
                    .login(credentials = viewModel.credentials)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        it?.let {
                            sharedPreferences.edit().putString("token",it.data.token).apply()

                            it.data.user.save()

                            view.showMessage("Logged in successfully.")
                        }
                    },{
                        view.showError(it.message!!)
                    })
        }
    }


}