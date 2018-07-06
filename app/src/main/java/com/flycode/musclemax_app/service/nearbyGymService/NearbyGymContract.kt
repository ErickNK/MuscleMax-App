package com.flycode.musclemax_app.service.nearbyGymService

import android.os.Bundle
import com.flycode.musclemax_app.ui.base.BaseServiceContract
import com.google.android.gms.maps.model.LatLng

interface NearbyGymContract{
    interface NearbyGymService : BaseServiceContract.Service{
        val nearbyDistance: Int
        var isWaitingForNearbyGyms: Boolean
        val lastKnownLocation: LatLng?
        fun onUpdateUI(latLng: LatLng)

    }

    interface NearbyGymServicePresenter<S : NearbyGymService> : BaseServiceContract.Presenter<S>{
        fun onLocationChanged(latLng: LatLng)
        fun onNearbyDistanceChanged()
    }
}

