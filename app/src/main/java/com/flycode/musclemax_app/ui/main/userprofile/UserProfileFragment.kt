package com.flycode.musclemax_app.ui.main.userprofile


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flycode.musclemax_app.R
import com.flycode.musclemax_app.databinding.UserProfileBinding
import com.flycode.musclemax_app.ui.base.BaseFragment
import com.squareup.picasso.Picasso
import javax.inject.Inject

class UserProfileFragment 
    : BaseFragment<UserProfileFragment, UserProfilePresenter, UserProfileViewModel>(),
        UserProfileContract.UserProfileFragment{

    @Inject
    override lateinit var viewModel: UserProfileViewModel
    lateinit var userProfileBinding : UserProfileBinding
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        userProfileBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_profile,container,false)
        userProfileBinding.viewModel = viewModel
        userProfileBinding.setLifecycleOwner(this)

        return userProfileBinding.root
    }


    fun loadUserProfilePicture(local_location: String){
        Picasso.get()
                .load(local_location)
                .into(userProfileBinding.imPicture)
    }
}
