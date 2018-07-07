package com.flycode.musclemax_app.ui.auth.signup.LocationDetails

import com.flycode.musclemax_app.ui.base.BaseContract

interface LocationDetailsContract {
    interface LocationDetailsFragment : BaseContract.View{
        
    }
    interface LocationDetailsPresenter<V : LocationDetailsFragment> : BaseContract.Presenter<V>{
        
    }
}