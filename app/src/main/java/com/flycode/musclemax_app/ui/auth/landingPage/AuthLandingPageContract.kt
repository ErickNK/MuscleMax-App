package com.flycode.musclemax_app.ui.auth.landingPage

import com.facebook.FacebookCallback
import com.facebook.login.LoginResult
import com.flycode.musclemax_app.ui.base.BaseContract
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

class AuthLandingPageContract {
    interface AuthLandingPageFragment : BaseContract.View
    interface AuthLandingPagePresenter<V : AuthLandingPageContract.AuthLandingPageFragment> : BaseContract.Presenter<V>{
        fun signInWithGoogle()
        fun signInWithFacebookCallback(): FacebookCallback<LoginResult>
        fun handleSignInWithGoogleResult(task: Task<GoogleSignInAccount>?)
    }
}