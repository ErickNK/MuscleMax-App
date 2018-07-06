package com.flycode.musclemax_app.service.nearbyGymService

import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import com.flycode.musclemax_app.trackers.DistanceToDestinationTracker
import com.flycode.musclemax_app.trackers.GpsTracker
import com.flycode.musclemax_app.ui.base.BaseService
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class NearbyGymService
    : BaseService<NearbyGymService, NearbyGymPresenter<NearbyGymService>>(),
        NearbyGymContract.NearbyGymService,
        DistanceToDestinationTracker.DistanceChangedListener,
        GpsTracker.GpsTrackerListener{
    @Inject
    override lateinit var presenter: NearbyGymPresenter<NearbyGymService>
    @Inject lateinit var distanceToDestinationTracker: DistanceToDestinationTracker
    @Inject lateinit var gpsTracker: GpsTracker
    private val mBinder = NearbyGymServiceBinder()
    override var isWaitingForNearbyGyms: Boolean = false

    override fun onCreate() {
        super.onCreate()
        instance = this

        //NO SLEEP
//        wakeLock(true,"UserService")

        //ON START BROADCAST
        sendBroadcast(Intent(ON_START_BROADCAST))
    }

    override fun onDestroy() {
        super.onDestroy()
//        wakeLock(false)

        listeners.clear()
        instance = null
    }

    /*############################### CALLBACKS ################################*/

    override fun onDistanceChanged(distance: Int, polyline: List<LatLng>) {

    }

    override fun onLocationChanged(location: LatLng) {
        if (isWaitingForNearbyGyms)
            presenter.onLocationChanged(location)
    }

    override fun onTrackerError(message: String) {
        listeners.forEach {
            it.onError(message)
        }
    }

    override fun onUpdateUI(latLng: LatLng) {
        listeners.forEach {
            it.onUpdateUI(Bundle().apply {
                this.putDouble("lat",latLng.latitude)
                this.putDouble("lng",latLng.longitude)
            })
        }
    }


    /*############################### VIEW INTERFACE ################################*/

    override val lastKnownLocation: LatLng?
        get() = gpsTracker.lastKnownLocation

    override var nearbyDistance: Int = 0
        set(value){
            if(field != value){
                field = value
                presenter.onNearbyDistanceChanged()
            }
        }

    /*############################### BINDING ################################*/

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return mBinder
    }

    override fun onUnbind(intent: Intent): Boolean {
        listeners.clear()
        return super.onUnbind(intent)
    }

    inner class NearbyGymServiceBinder : Binder() {
        val service: NearbyGymService
            get() = this@NearbyGymService
    }

    companion object {
        const val ON_START_BROADCAST = "com.flycode.muclemax_app.nearbyGymService.onStart"
        private var instance: NearbyGymService? = null
        val isInstanceCreated : Boolean
            get() = instance != null
    }
}

