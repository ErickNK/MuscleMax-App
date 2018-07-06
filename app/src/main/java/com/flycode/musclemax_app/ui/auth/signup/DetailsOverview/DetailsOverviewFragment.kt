package com.flycode.musclemax_app.ui.auth.signup.DetailsOverview

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.flycode.musclemax_app.R
import com.flycode.musclemax_app.databinding.DetailsOverviewBinding
import com.flycode.musclemax_app.ui.auth.signup.SignUpFragment
import com.flycode.musclemax_app.ui.auth.signup.SignUpViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_details_overview.*
import javax.inject.Inject

class DetailsOverviewFragment : DaggerFragment() {

    @Inject lateinit var superViewModel: SignUpViewModel
    @Inject lateinit var signUpFragment: SignUpFragment
    lateinit var detailsOverviewBinding : DetailsOverviewBinding

    companion object {

        fun newInstance(): DetailsOverviewFragment {
            val fragment = DetailsOverviewFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        detailsOverviewBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_details_overview, container, false)
        detailsOverviewBinding.superViewModel = superViewModel
        return detailsOverviewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_middle.setOnClickListener{
            signUpFragment.onFinish()
        }
        btn_prev.setOnClickListener{
            NavHostFragment.findNavController(this).navigate(R.id.HeightDetailsFragment)
        }
        setupProfilePic()
    }

    fun setupProfilePic(){
        superViewModel.imageBitmap?.let {
            im_picture.maxWidth = 200
            im_picture.setImageBitmap(it)
        }
    }
}// Required empty public constructor
