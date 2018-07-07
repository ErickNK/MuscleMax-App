package com.flycode.musclemax_app.trackers.filters

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationProvider
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.flycode.musclemax_app.R

/**
 *
 * This is a helper class that is used to determine when the GPS status is good
 * enough (isFixed())
 *
 */
class GpsStatusFilter(
        val context: Context
) : LocationListener, android.location.GpsStatus.Listener {

    private var mHistory: Array<Location?>? = null
    private var locationManager: LocationManager? = null
    var listener: TickListener? = null

    /**
     * If we get a location with accurancy <= mFixAccurancy mFixed => true
     */
    private val mFixAccurancy = 10f

    /**
     * If we get fixed satellites >= mFixSatellites mFixed => true
     */
    private val mFixSatellites = 2

    /**
     * If we get location updates with time difference <= mFixTime mFixed =>
     * true
     */
    private val mFixTime = 3

    private var satellitesAvailable = 0
    private var satellitesFixed = 0

    val isLogging: Boolean
        get() = locationManager != null

    val isEnabled: Boolean
        get() {
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }

    init {
        mHistory = arrayOfNulls(HIST_LEN)
    }

    fun start() {
        clear(true)
        if (ContextCompat.checkSelfPermission(this.context,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
            } catch (ex: Exception) {
                if (ex.message != null){
                    listener?.onFilterError(ex.message!!)
                }else{
                    listener?.onFilterError(context.getString(R.string.something_went_wrong))
                }
            }
            locationManager.addGpsStatusListener(this)
        }
    }

    /**
     * Release the resources and listener.
     */
    fun stop() {
        if (locationManager != null) {
            locationManager!!.removeGpsStatusListener(this)

            try {
                locationManager!!.removeUpdates(this)
            } catch (ex: SecurityException) {
                //Ignore if user turn off GPS
            }

        }
        locationManager = null
    }

    override fun onLocationChanged(location: Location) {
        //add new location to the front
        System.arraycopy(mHistory!!, 0, mHistory!!, 1, HIST_LEN - 1)
        mHistory!![0] = location

        //The lesser the accuracy the better
        if (location.hasAccuracy() && location.accuracy < mFixAccurancy) {
            listener!!.isFixed = true

            //The lesser the update time the better
        } else if (mHistory!![1] != null && location.time - mHistory!![1]?.time!! <= 1000 * mFixTime) {
            listener!!.isFixed = true

            //The more the satellites the better
        } else if (satellitesAvailable >= mFixSatellites) {
            listener!!.isFixed = true
        }
    }

    /**
     * When the provider is disabled clear
     */
    override fun onProviderDisabled(provider: String) {
        if (provider.equals("gps", ignoreCase = true)) {
            clear(true)
        }
    }

    /**
     * When the provider is operational don't clear
     */
    override fun onProviderEnabled(provider: String) {
        if (provider.equals("gps", ignoreCase = true)) {
            clear(false)
        }
    }

    /**
     * When the provider is out of service or unavailable clear the check
     *
     */
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        if (provider.equals("gps", ignoreCase = true)) {
            if (status == LocationProvider.OUT_OF_SERVICE || status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
                clear(true)
            }
        }
    }

    /**
     * Count the number of satellites available and the number of satellites that
     * where used in the last GPS fix.
     *
     */
    override fun onGpsStatusChanged(event: Int) {
        if (locationManager == null)
            return


        @SuppressLint("MissingPermission")
        val gpsStatus = locationManager!!.getGpsStatus(null)
                ?: return

        var cnt0 = 0
        var cnt1 = 0
        val list = gpsStatus.satellites
        for (satellite in list) {
            cnt0++
            if (satellite.usedInFix()) {
                cnt1++
            }
        }
        satellitesAvailable = cnt0
        satellitesFixed = cnt1
    }

    /**
     * Clear all satellites counted and the the history
     */
    private fun clear(resetIsFixed: Boolean) {
        if (resetIsFixed) {
            listener!!.isFixed = false
        }
        satellitesAvailable = 0
        satellitesFixed = 0
        mHistory = arrayOfNulls(HIST_LEN)
    }

    interface TickListener {
        var isFixed : Boolean
        fun onFilterError(message: String)
    }

    companion object {

        private val HIST_LEN = 3
    }
}


