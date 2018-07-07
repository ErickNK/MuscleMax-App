package com.flycode.musclemax_app.ui.main.gym.gymView

import android.os.Bundle
import com.flycode.musclemax_app.ui.base.BaseContract

interface GymViewContract {
    interface GymViewFragment : BaseContract.View{

    }
    interface GymViewPresenter<V : GymViewFragment> : BaseContract.Presenter<V>{
        
    }
}