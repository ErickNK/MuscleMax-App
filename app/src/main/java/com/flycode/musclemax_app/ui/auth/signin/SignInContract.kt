package com.flycode.musclemax_app.ui.auth.signin

import com.flycode.musclemax_app.data.models.Credentials
import com.flycode.musclemax_app.ui.base.BaseContract

interface SignInContract {
    interface SignInFragment : BaseContract.View
    interface SignInPresenter<V : SignInFragment> : BaseContract.Presenter<V>{
        fun login()
    }
    interface SignInViewModel<V : SignInFragment, P : SignInPresenter<V>> : BaseContract.ViewModel<V,P>{
        val credentials : Credentials
    }
}