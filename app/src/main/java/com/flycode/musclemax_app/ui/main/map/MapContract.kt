package com.flycode.musclemax_app.ui.main.map

import android.os.Bundle
import com.flycode.musclemax_app.ui.base.BaseContract

interface MapContract {
    interface MapFragment : BaseContract.View{
      
    }
    interface MapPresenter<V : MapFragment> : BaseContract.Presenter<V>{
        fun init()

        fun registerNearbyGymServiceStartedBroadcast()

        fun unRegisterNearbyGymServiceStartedBroadcast()

        fun syncWithNearbyGymService()

        fun unSyncWithNearbyGymService()

        fun startNearbyGymService()

        fun onLocationUpdate(data: Bundle)
    }
}