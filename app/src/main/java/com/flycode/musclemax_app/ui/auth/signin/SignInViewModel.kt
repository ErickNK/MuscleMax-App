package com.flycode.musclemax_app.ui.auth.signin

import com.flycode.musclemax_app.data.models.Credentials
import com.flycode.musclemax_app.ui.base.BaseViewModel

class SignInViewModel: BaseViewModel<SignInFragment, SignInPresenter>()
        , SignInContract.SignInViewModel<SignInFragment,SignInPresenter>{

    override val credentials: Credentials = Credentials()
}
