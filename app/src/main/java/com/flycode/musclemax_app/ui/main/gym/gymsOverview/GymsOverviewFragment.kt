package com.flycode.musclemax_app.ui.main.gym.gymsOverview


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flycode.musclemax_app.R
import com.flycode.musclemax_app.ui.base.BaseFragment
import javax.inject.Inject


class GymsOverviewFragment
    : BaseFragment<GymsOverviewFragment, GymsOverviewPresenter, GymsOverviewViewModel>(),
        GymsOverviewContract.GymsOverviewFragment {

    @Inject override lateinit var viewModel: GymsOverviewViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gyms_overview, container, false)
    }


}
