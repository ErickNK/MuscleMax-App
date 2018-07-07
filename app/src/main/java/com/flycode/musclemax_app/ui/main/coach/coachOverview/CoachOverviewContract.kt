package com.flycode.musclemax_app.ui.main.coach.coachOverview

import com.flycode.musclemax_app.ui.base.BaseContract

interface CoachOverviewContract{
    interface CoachOverviewFragment : BaseContract.View{

    }
    interface CoachOverviewPresenter<V : CoachOverviewFragment> : BaseContract.Presenter<V>{

    }
}