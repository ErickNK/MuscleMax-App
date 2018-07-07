package com.flycode.musclemax_app.trackers

import android.Manifest
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.location.LocationManager.NETWORK_PROVIDER
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.flycode.musclemax_app.R
import com.flycode.musclemax_app.trackers.filters.GpsStatusFilter
import com.google.android.gms.maps.model.LatLng

class GpsTracker(
        val context: Context
) : LocationListener,
        GpsStatusFilter.TickListener,
        LifecycleObserver {

    override var isFixed: Boolean = false
    private var isGPSEnabled = false
    private var isUsingGps = false
    private var isNetworkEnabled = false
    private var isUsingNetwork = false
    private var locationManager: LocationManager? = null
    private lateinit var gpsStatusFilter: GpsStatusFilter
    var listener: GpsTrackerListener? = null

    val lastKnownLocation: LatLng?
        get() =
            if (ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    val providers = locationManager!!.getProviders(true)
                    var bestLocation: Location? = null
                    for (provider in providers) {
                        val l = locationManager!!.getLastKnownLocation(provider)
                        Log.d(TAG, String.format("last known location, provider: %s, location: %s", provider, l))

                        if (l == null) {
                            continue
                        }
                        if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                            Log.d(TAG, String.format("found best last known location: %s", l))
                            bestLocation = l
                        }
                    }
                bestLocation?.let {
                    LatLng(bestLocation.latitude,bestLocation.longitude)
                }
            } else null

    /*############################### TRACKER_COMPONENT ################################*/

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onInit() {
        try {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager == null) {
                listener?.onTrackerError("LocationManager == null")
            }

            isGPSEnabled = locationManager!!.isProviderEnabled(GPS_PROVIDER)
            isNetworkEnabled = locationManager!!.isProviderEnabled(NETWORK_PROVIDER)

            if (!isGPSEnabled && !isNetworkEnabled) {
                listener?.onTrackerError("!isGPSEnabled && !isNetworkEnabled")
            }

            gpsStatusFilter = GpsStatusFilter(context).apply {
                this.listener = this@GpsTracker
            }
        } catch (ex: Exception) {
            if (ex.message != null){
                listener?.onTrackerError(ex.message!!)
            }else{
                listener?.onTrackerError(context.getString(R.string.something_went_wrong))
            }
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        try {
            locationManager!!.removeUpdates(this)
            gpsStatusFilter.stop()
        } catch (ex: SecurityException) {
            //Ignore if user turn off GPS
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onPlay() {
        if (ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //ON GPS
            if (isGPSEnabled) {
                locationManager!!.requestLocationUpdates(
                        GPS_PROVIDER,
                        10000,
                        10f,
                        this
                )
                isUsingGps = true
                isUsingNetwork = false
                gpsStatusFilter.start()
            } else if (isNetworkEnabled) {
                locationManager!!.requestLocationUpdates(
                        NETWORK_PROVIDER,
                        10000,
                        10f,
                        this
                )
                isUsingGps = false
                isUsingNetwork = true
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onFinish() {
        locationManager = null
    }

    override fun onLocationChanged(p0: Location?) {
        if ((isUsingGps && isFixed) || isUsingNetwork)
            p0?.let {
                listener?.onLocationChanged(LatLng(it.latitude,it.longitude))
            }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

    override fun onProviderEnabled(p0: String?) {}

    override fun onProviderDisabled(p0: String?) {}

    override fun onFilterError(message: String) {
        listener?.onTrackerError(message)
    }

    interface GpsTrackerListener {
        fun onLocationChanged(location: LatLng)
        fun onTrackerError(message: String)
    }

    companion object {
        private val TAG = GpsTracker::class.java.simpleName
    }
}


