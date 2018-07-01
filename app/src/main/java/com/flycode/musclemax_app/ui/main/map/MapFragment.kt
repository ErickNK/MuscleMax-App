package com.flycode.musclemax_app.ui.main.map


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flycode.musclemax_app.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import dagger.android.support.DaggerFragment

class MapFragment
    : DaggerFragment(),
        OnMapReadyCallback {

    private val PERMISSION_REQUEST_CODE = 0
    private var mMap: GoogleMap? = null
    private var permissionsGranted = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // GOOGLE MAPS
        checkLocationPermissions()
        isGooglePlayServicesAvailable()
        val mapFragment = fragmentManager?.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (permissionsGranted) {
            mMap?.isMyLocationEnabled = true
        }
    }


    /**
     * Check if the application has been granted access to the camera.
     * If not hide the progress image card view and try requesting for it.
     *
     */
    private fun checkLocationPermissions() {
        context?.let {
            if ( ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(it, Manifest.permission.INTERNET)
                    != PackageManager.PERMISSION_GRANTED) {
                activity?.let { it1 ->
                    ActivityCompat.requestPermissions(it1,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET),
                            PERMISSION_REQUEST_CODE)
                }
            } else
                permissionsGranted = true
        }
    }

    /**
     * Checking if Google Play Services Available or not
     */
    private fun isGooglePlayServicesAvailable(): Boolean {
        val googleAPI = GoogleApiAvailability.getInstance()
        val result = googleAPI.isGooglePlayServicesAvailable(context)
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(activity, result, 0).show()
            }
            return false
        }
        return true
    }

}
