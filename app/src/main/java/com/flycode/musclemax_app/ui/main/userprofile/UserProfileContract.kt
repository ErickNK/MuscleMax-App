package com.flycode.musclemax_app.ui.main.userprofile

import com.flycode.musclemax_app.ui.base.BaseContract

interface UserProfileContract {
    interface UserProfileFragment : BaseContract.View{

    }
    interface UserProfilePresenter<V : UserProfileFragment> : BaseContract.Presenter<V>{
      
    }
}