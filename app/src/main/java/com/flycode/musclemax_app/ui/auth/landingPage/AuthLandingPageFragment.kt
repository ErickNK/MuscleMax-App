package com.flycode.musclemax_app.ui.auth.landingPage


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.flycode.musclemax_app.R
import com.flycode.musclemax_app.ui.auth.signup.SignUpFragment
import com.flycode.musclemax_app.ui.auth.signup.SignUpViewModel
import com.flycode.musclemax_app.ui.base.BaseFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.android.synthetic.main.fragment_auth_landing_page.*
import java.util.*
import javax.inject.Inject

class AuthLandingPageFragment 
    : BaseFragment<AuthLandingPageFragment, AuthLandingPagePresenter, AuthLandingPageViewModel>(),
        AuthLandingPageContract.AuthLandingPageFragment{

    @Inject
    override lateinit var viewModel: AuthLandingPageViewModel
    private val callbackManager : CallbackManager = CallbackManager.Factory.create()

    companion object {
        const val SIGN_IN_BY_GOOGLE_RESPONSE_CODE = 1

        fun newInstance() : AuthLandingPageFragment{
            return AuthLandingPageFragment()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_auth_landing_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_google.setOnClickListener{
            presenter.signInWithGoogle()
        }
        LoginManager.getInstance().registerCallback(callbackManager, presenter.signInWithFacebookCallback())
        btn_facebook.setOnClickListener{
            showLoading()
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(
                    "public_profile","email"
            ))
        }
        sign_in_btn.setOnClickListener{
            NavHostFragment.findNavController(this).navigate(R.id.signInFragment)
        }

        sign_up_btn.setOnClickListener{
            NavHostFragment.findNavController(this).navigate(R.id.signUpFragment)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == SIGN_IN_BY_GOOGLE_RESPONSE_CODE) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            presenter.handleSignInWithGoogleResult(task)
        }
    }

}
