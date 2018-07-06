package com.flycode.musclemax_app.ui.auth.signin

import android.content.SharedPreferences
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloMutationCall
import com.apollographql.apollo.rx2.Rx2Apollo
import com.flycode.musclemax_app.LoginMutation
import com.flycode.musclemax_app.data.models.LoginPayload
import com.flycode.musclemax_app.ui.base.BasePresenter
import com.flycode.musclemax_app.ui.main.MainActivity
import com.google.gson.Gson
import com.raizlabs.android.dbflow.kotlinextensions.save
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SignInPresenter(
        val apolloClient: ApolloClient,
        val sharedPreferences: SharedPreferences
) : BasePresenter<SignInFragment,SignInPresenter,SignInViewModel>(),
        SignInContract.SignInPresenter<SignInFragment>{

    override fun login(){
        view?.let { view ->
            compositeDisposable.add(
                    Rx2Apollo.from(loginUser())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe ({
                                it?.data()?.let {it1 ->
                                    val loginPayload : LoginPayload = Gson()
                                            .fromJson(
                                                    Gson().toJson(it1.login()!!)
                                                    , LoginPayload::class.java
                                            )
                                    autoRegister(loginPayload)
                                }
                            },{
                                view.hideLoading()
                                if (it.message != null){
                                    view.showError(message = it.message.toString())
                                }else{
                                    view.showError("Something went wrong. Please try again.")
                                }
                            })
            )
        }
    }

    private fun loginUser(): ApolloMutationCall<LoginMutation.Data> {
        return apolloClient.mutate(
                LoginMutation.builder()
                        .email(viewModel.credentials.email)
                        .password(viewModel.credentials.password)
                        .build()
        )
    }

    private fun autoRegister(loginPayload: LoginPayload){
        Observable.create<Boolean> {
            sharedPreferences.edit().putString("token",loginPayload.token).apply()
            loginPayload.user._tag = "default_user"
            if (loginPayload.user.save()){
                it.onNext(true)
            }else it.onError(Throwable("Something Went Wrong"))
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    view?.showMessage("Successfully Authenticated")
                    view?.navigateToActivity(view?.context, MainActivity::class.java)

                },{
                    view?.hideLoading()
                    if (it.message != null){
                        view?.showError(message = it.message.toString())
                    }else{
                        view?.showError("Something went wrong. Please try again.")
                    }
                })
    }


}