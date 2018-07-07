package com.flycode.musclemax_app.ui.main.coach.coachView

import com.flycode.musclemax_app.ui.base.BaseContract

interface CoachViewContract {
    interface CoachViewFragment : BaseContract.View{

    }
    interface CoachViewPresenter<V : CoachViewFragment> : BaseContract.Presenter<V>{

    }
}