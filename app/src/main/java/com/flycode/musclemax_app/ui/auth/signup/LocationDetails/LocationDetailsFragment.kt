package com.flycode.musclemax_app.ui.auth.signup.LocationDetails


import android.Manifest
import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import androidx.navigation.fragment.NavHostFragment
import com.flycode.musclemax_app.R
import com.flycode.musclemax_app.trackers.GpsTracker
import com.flycode.musclemax_app.ui.auth.signup.SignUpViewModel
import com.flycode.musclemax_app.ui.base.BaseFragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_location_details.*
import javax.inject.Inject


class LocationDetailsFragment
    : BaseFragment<LocationDetailsFragment, LocationDetailsPresenter, LocationDetailsViewModel>(),
        LocationDetailsContract.LocationDetailsFragment, OnMapReadyCallback, GpsTracker.GpsTrackerListener, GoogleApiClient.OnConnectionFailedListener {


    @Inject override lateinit  var viewModel: LocationDetailsViewModel
    @Inject lateinit  var superViewModel: SignUpViewModel
    @Inject lateinit var gpsTracker: GpsTracker
    @Inject lateinit var placeAutocompleteAdapter: PlaceAutocompleteAdapter
    private val PERMISSION_REQUEST_CODE = 0
    private val GOOGLE_SERVICES_ERROR_REQUEST_CODE = 1
    var googleApiClient: GoogleApiClient? = null
    private lateinit var map: GoogleMap
    private var locationPermissionsGranted = false
    private var input_finish_delay: Long = 1000 // 1 seconds after user stops typing
    private var input_finish_handler = Handler()
    private val input_finish_checker = Runnable {
        if (System.currentTimeMillis() > viewModel.lastTextEdit + input_finish_delay - 500) {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(googleApiClient == null)
            googleApiClient = GoogleApiClient
                    .Builder(context!!)
                    .addApi(Places.GEO_DATA_API)
                    .enableAutoManage(activity!!,this)
                    .build()

        ed_search.setAdapter(placeAutocompleteAdapter)

        ed_search.setOnClickListener { showSoftKeyboard() }

        ed_search.setOnItemClickListener{ adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            hideSoftKeyboard()
            presenter.onSuggestionItemPicked(adapterView,view1,i,l)
        }

        if(isGooglePlayServicesAvailable()) {
            checkLocationPermissions()
        }

        btn_prev.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.PersonalDetailsFragment)
        }

        btn_middle.setOnClickListener {
            ed_search.text.clear()
            map.clear()
            superViewModel.doSaveLocation = false
            NavHostFragment.findNavController(this).navigate(R.id.WeightDetailsFragment)
        }
        btn_next.setOnClickListener{
            NavHostFragment.findNavController(this).navigate(R.id.WeightDetailsFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        if(!googleApiClient?.isConnected!!){
            googleApiClient?.connect()
        }
    }
    override fun onPause() {
        super.onPause()
        googleApiClient?.stopAutoManage(activity!!)
        googleApiClient?.disconnect()
    }

    override fun onDestroy() {
        super.onDestroy()
        googleApiClient = null
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.isMyLocationEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = false
        isWaitingForMap = false

        gpsTracker.lastKnownLocation?.let {
            moveCamera(it,13f)
        }

        btn_current_location.setOnClickListener {
            //Senter on user
            gpsTracker.lastKnownLocation?.let {
                moveCamera(it,13f)
            }
        }

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(context, R.raw.map_styles)
            )

            if (!success) {
                showError(resources.getString(R.string.something_went_wrong))
            }
        } catch (e: Resources.NotFoundException) {
           showError(resources.getString(R.string.something_went_wrong))
        }

    }

    fun moveCamera(latLng: LatLng,zoom: Float,mark: Boolean = false,markerTitle: String = ""){
        if (!isWaitingForMap){
            map.clear()
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom))
            if (mark)
                map.addMarker(
                        MarkerOptions()
                                .title(markerTitle)
                                .snippet("lat:${latLng.latitude}, lng:${latLng.longitude}")
                                .position(latLng)
                )
        }
    }

    private fun mapsInit(){
        val mapFragment = childFragmentManager.findFragmentById(R.id.location_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onLocationChanged(location: LatLng) {
        moveCamera(location,13f)
    }

    override fun onTrackerError(message: String) {
        showError(message)
    }

    private var isWaitingForMap: Boolean = true

    override fun onPermissionsGranted(requestCode: Int) {
        super.onPermissionsGranted(requestCode)
        locationPermissionsGranted = true
        isWaitingForMap = true
        mapsInit()
    }

    private fun hideSoftKeyboard(){
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    private fun showSoftKeyboard(){
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        if(p0.errorMessage !=  null){
            showError(p0.errorMessage!!)
        }else{
            showError(resources.getString(R.string.something_went_wrong))
        }

    }

    /**
     * Check if the application has been granted access to the camera.
     * If not hide the progress image card view and try requesting for it.
     *
     */
    private fun checkLocationPermissions() {
        requestAppPermissions(
                arrayOf(
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ),
                R.string.we_need_permission_to_function, PERMISSION_REQUEST_CODE)
    }

    /**
     * Checking if Google Play Services Available or not
     */
    private fun isGooglePlayServicesAvailable(): Boolean {
        val googleAPI = GoogleApiAvailability.getInstance()
        val result = googleAPI.isGooglePlayServicesAvailable(context)
        return if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(activity, result, GOOGLE_SERVICES_ERROR_REQUEST_CODE).show()
                false
            } else {
                showError(getString(R.string.google_services_error))
                false
            }
        }else true
    }
}
