package com.flycode.musclemax_app.ui.auth.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.flycode.musclemax_app.R
import com.flycode.musclemax_app.data.models.User
import com.flycode.musclemax_app.ui.base.BaseFragment
import kotlinx.android.synthetic.main.activity_auth.*
import javax.inject.Inject

class SignUpFragment
    : BaseFragment<SignUpFragment,SignUpPresenter,SignUpViewModel>(),
        SignUpContract.SignUpFragment{

    @Inject override lateinit var viewModel: SignUpViewModel
    @Inject lateinit var viewPagerAdapter: ViewPagerAdapter
    private var currentPage: Int = 0

    companion object {
        const val UN_REGISTERED_USER = "un_registered_user"
        const val EXTERNAL_AUTH_TYPE = "external_auth_type"
        fun newInstance() = SignUpFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sign_up_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(arguments?.getSerializable(UN_REGISTERED_USER)  != null && arguments?.getString(EXTERNAL_AUTH_TYPE) != null ){
            val user : User = arguments?.getSerializable(UN_REGISTERED_USER) as User
            viewModel.user.apply {
                this.firstname = user.firstname
                this.lastname = user.lastname
                this.age = user.age
                this.email = user.email
                this.gender = user.gender
                this.tel = user.tel
                this.password = "000000"
                this.pictures.apply {
                    this.add(com.flycode.musclemax_app.data.models.Picture().apply {
                        this.remote_location = user.pictures[0].remote_location
                    })
                }
            }
            when (arguments?.getString(EXTERNAL_AUTH_TYPE)){
                "google" -> viewModel.uiState.googleLoginSuccess = true
                "facebook" -> viewModel.uiState.facebookLoginSuccess = true
            }
        }
    }

    override fun onSignIn(){
        NavHostFragment.findNavController(auth_nav_fragment).navigate(R.id.signInFragment)
    }
    override fun onFinish(){
        presenter.onFinish()
    }

}
