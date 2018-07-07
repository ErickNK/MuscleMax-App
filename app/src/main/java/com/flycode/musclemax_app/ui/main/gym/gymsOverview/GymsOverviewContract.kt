package com.flycode.musclemax_app.ui.main.gym.gymsOverview

import com.flycode.musclemax_app.ui.base.BaseContract

interface GymsOverviewContract {
    interface GymsOverviewFragment : BaseContract.View{

    }
    interface GymsOverviewPresenter<V : GymsOverviewFragment> : BaseContract.Presenter<V>{

    }
}