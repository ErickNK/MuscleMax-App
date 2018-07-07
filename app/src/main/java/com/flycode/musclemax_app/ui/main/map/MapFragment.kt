package com.flycode.musclemax_app.ui.main.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.annotation.NonNull
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import com.afollestad.materialdialogs.MaterialDialog
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.flycode.musclemax_app.R
import com.flycode.musclemax_app.data.models.Gym
import com.flycode.musclemax_app.databinding.CustomDistanceEntryBinging
import com.flycode.musclemax_app.databinding.MapFragmentBinding
import com.flycode.musclemax_app.ui.base.BaseFragment
import com.flycode.musclemax_app.ui.base.BaseServiceContract
import com.flycode.musclemax_app.ui.flexibleItems.GymListItem
import com.flycode.musclemax_app.ui.flexibleItems.SearchResultsHeaderItem
import com.flycode.musclemax_app.ui.main.MainActivity
import com.flycode.musclemax_app.ui.main.gym.gymView.GymViewFragment
import com.github.reline.GoogleMapsBottomSheetBehavior
import com.github.reline.GoogleMapsBottomSheetBehavior.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import eu.davidea.flexibleadapter.utils.Log
import nz.co.trademe.mapme.annotations.OnInfoWindowClickListener
import nz.co.trademe.mapme.annotations.OnMapAnnotationClickListener
import javax.inject.Inject

class MapFragment
    : BaseFragment<MapFragment, MapPresenter, MapViewModel>(),
        MapContract.MapFragment,
        OnMapReadyCallback, BaseServiceContract.ServiceListener, GymListItem.GymListItemListener {

    private val PERMISSION_REQUEST_CODE = 0
    lateinit var mapFragmentBinding: MapFragmentBinding
    private lateinit var map: GoogleMap
    @Inject override lateinit var viewModel: MapViewModel
    @Inject lateinit var mapsAdapter: MapsAdapter
    @Inject lateinit var mainActivity: MainActivity
    @Inject lateinit var gymImagesPager: GymImagesPager
    lateinit var behavior: GoogleMapsBottomSheetBehavior<NestedScrollView>
    @Inject lateinit var searchListAdapter: FlexibleAdapter<SearchResultsHeaderItem>
    var bottomSheetStateListener: BottomSheetStateListener? = null

    lateinit var customDistanceEntryBinging: CustomDistanceEntryBinging
    lateinit var customDistanceEntryDialog: MaterialDialog

    private var permissionsGranted = false
    private var input_finish_delay: Long = 1000 // 1 seconds after user stops typing
    private var input_finish_handler = Handler()
    private val input_finish_checker = Runnable {
        if (System.currentTimeMillis() > viewModel.lastTextEdit + input_finish_delay - 500) {
            presenter.searchGyms(mapFragmentBinding.toolbar.findViewById<EditText>(R.id.ed_search).text.toString())
            onOpenSearch()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mapFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_map,container,false)
        mapFragmentBinding.viewModel = viewModel
        mapFragmentBinding.setLifecycleOwner(this)

        return mapFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(mapFragmentBinding.toolbar as Toolbar)

        // GOOGLE MAPS
        checkLocationPermissions()
        isGooglePlayServicesAvailable()
        val mapFragment = childFragmentManager.findFragmentById(R.id.main_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mapFragmentBinding.toolbar.findViewById<EditText>(R.id.ed_search).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
                //You need to remove this to run only once
                input_finish_handler.removeCallbacks(input_finish_checker)

            }

            override fun afterTextChanged(s: Editable) {
                //avoid triggering event when text is empty
                if (s.isNotEmpty()) {
                    viewModel.lastTextEdit = System.currentTimeMillis()
                    input_finish_handler.postDelayed(input_finish_checker, input_finish_delay)
                }
            }
        })
        mapFragmentBinding.toolbar.findViewById<ImageButton>(R.id.btn_humberger).setOnClickListener {
            if (viewModel.uiState.isSearchOpen){ //if not open
                onCloseSearch()
            }else{
                mainActivity.toggleDrawer()
            }
        }

        mapFragmentBinding.mapReloadBtn.setOnClickListener {
            presenter.refetchNearbyGyms()
            viewModel.uiState.onMapError = false
        }

        mapFragmentBinding.searchReloadBtn.setOnClickListener {
            presenter.searchGyms(mapFragmentBinding.toolbar.findViewById<EditText>(R.id.ed_search).text.toString())
            viewModel.uiState.onSearchError = false
        }

        mapFragmentBinding.btnDistance.setOnClickListener{
            customDistanceEntryDialog.show()
        }
        setupMapsBottomSheet()
        setupSearchRecyclerView()
        setupDistanceEntry()
    }

    private fun setupMapsBottomSheet(){
        behavior = GoogleMapsBottomSheetBehavior.from(mapFragmentBinding.bottomsheet)
        mapFragmentBinding.parallaxViewPager.adapter = gymImagesPager

        behavior.parallax = mapFragmentBinding.parallaxViewPager

        // wait for the bottomsheet to be laid out
        mapFragmentBinding.bottomsheet.viewTreeObserver.addOnGlobalLayoutListener (object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                // set the height of the parallax to fill the gap between the anchor and the top of the screen
                val layoutParams : CoordinatorLayout.LayoutParams =
                        CoordinatorLayout.LayoutParams(mapFragmentBinding.parallaxViewPager.measuredWidth, behavior.anchorOffset)
                mapFragmentBinding.parallaxViewPager.layoutParams = layoutParams
                mapFragmentBinding.bottomsheet.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        behavior.setBottomSheetCallback(object : GoogleMapsBottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(@NonNull  bottomSheet: View, newState: Int) {
                // each time the bottomsheet changes position, animate the camera to keep the pin in view
                // normally this would be a little more complex (getting the pin location and such),
                // but for the purpose of an example this is enough to show how to stay centered on a pin
//                map.animateCamera(CameraUpdateFactory.newLatLng(SYDNEY));
                when(newState){
                    STATE_EXPANDED ->{
                        onShowBottomSheetToolbar()
                        mapFragmentBinding.bottomSheetTaunt.apply {
                            this.circleBackgroundColor = resources?.getColor(R.color.colorWhite)!!
                            this.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp)
                        }
                        onHideAppBar()
                    }
                    STATE_ANCHORED -> {
                        mapFragmentBinding.bottomSheetTaunt.apply {
                            this.circleBackgroundColor = resources?.getColor(R.color.colorWhite)!!
                            this.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp)
                        }
                        onHideBottomSheetToolbar()
                        onHideAppBar()
                    }
                    STATE_COLLAPSED, STATE_HIDDEN -> {
                        mapFragmentBinding.bottomSheetTaunt.apply {
                            this.circleBackgroundColor = resources?.getColor(R.color.colorPrimary)!!
                            this.setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp)
                        }
                        onHideBottomSheetToolbar()
                        onShowAppBar()
                    }
                }
                bottomSheetStateListener?.onStateChanged(newState)
                viewModel.uiState.bottomSheetState = newState
            }

            override fun onSlide(@NonNull  bottomSheet: View,  slideOffset: Float) {

            }
        })

        bottomSheetStateListener = (childFragmentManager.findFragmentById(R.id.gym_view) as GymViewFragment)
    }

    private fun onHideAppBar(){
        if (!viewModel.uiState.isAppBarHidden){
            YoYo.with(Techniques.SlideOutUp)
                    .duration(500)
                    .repeat(0)
                    .onEnd {
                        mapFragmentBinding.appbarlayout.visibility = View.GONE
                    }
                    .playOn(mapFragmentBinding.appbarlayout)
            viewModel.uiState.isAppBarHidden = true
        }

    }

    private fun onShowAppBar(){
        if (viewModel.uiState.isAppBarHidden){
            YoYo.with(Techniques.SlideInDown)
                    .duration(500)
                    .repeat(0)
                    .onStart {
                        mapFragmentBinding.appbarlayout.visibility = View.VISIBLE
                    }
                    .playOn(mapFragmentBinding.appbarlayout)
            viewModel.uiState.isAppBarHidden = false
        }
    }

    private fun onShowBottomSheetToolbar(){
        if (viewModel.uiState.isBottomSheetToolbarHidden){
            YoYo.with(Techniques.SlideOutDown)
                    .duration(500)
                    .repeat(0)
                    .onEnd {
                        mapFragmentBinding.bottomSheetDetails.visibility = View.GONE
                    }
                    .playOn(mapFragmentBinding.bottomSheetDetails)

            YoYo.with(Techniques.SlideInDown)
                    .duration(500)
                    .repeat(0)
                    .onStart {
                        mapFragmentBinding.bottomSheetToolbar.visibility = View.VISIBLE
                    }
                    .playOn(mapFragmentBinding.bottomSheetToolbar)

            mapFragmentBinding.bottomSheetToolbarBackBtn.setOnClickListener {
                behavior.state = STATE_ANCHORED
            }
            viewModel.uiState.isBottomSheetToolbarHidden = false
        }
    }

    private fun onHideBottomSheetToolbar(){
        if (!viewModel.uiState.isBottomSheetToolbarHidden){
            YoYo.with(Techniques.SlideOutUp)
                    .duration(500)
                    .repeat(0)
                    .onEnd {
                        mapFragmentBinding.bottomSheetToolbar.visibility = View.GONE
                    }
                    .playOn(mapFragmentBinding.bottomSheetToolbar)

            YoYo.with(Techniques.SlideInUp)
                    .duration(500)
                    .repeat(0)
                    .onStart {
                        mapFragmentBinding.bottomSheetDetails.visibility = View.VISIBLE
                    }
                    .playOn(mapFragmentBinding.bottomSheetDetails)
            viewModel.uiState.isBottomSheetToolbarHidden = true
        }
    }

    /**
     * Setup the recycler view by adding the adapter
     * */
    private fun setupSearchRecyclerView(){
        //ADAPTER
        FlexibleAdapter.enableLogs(Log.Level.DEBUG)
        FlexibleAdapter.useTag("SearchAdapter")
        val adapter = searchListAdapter

        // Non-exhaustive configuration that don't need RV instance
        adapter.addListener(this) //Only if you didn't use the Constructor
                .setAnimationOnForwardScrolling(true) //Enable scrolling animation: entry + forward scrolling
                .setAnimationOnReverseScrolling(true) //Enable animation for reverse scrolling

        adapter.addItem(viewModel.searchResultHeaderItem)

        //LAYOUT MANAGER
        val layoutManager = SmoothScrollLinearLayoutManager(context)

        mapFragmentBinding.searchRecyclerView.layoutManager = layoutManager
        mapFragmentBinding.searchRecyclerView.setHasFixedSize(true)
        mapFragmentBinding.searchRecyclerView.adapter = adapter
    }

    private fun onOpenSearch(){
        if (!viewModel.uiState.isSearchOpen) {
            YoYo.with(Techniques.SlideInDown)
                    .duration(500)
                    .repeat(0)
//                    .onStart {
//                        search_results.visibility = View.VISIBLE
//                    }
                    .playOn(mapFragmentBinding.searchResults)

            YoYo.with(Techniques.FadeOut)
                    .duration(500)
                    .delay(100)
                    .repeat(0)
//                    .onEnd {
//                        details_frame.visibility = View.GONE
//                    }
                    .playOn(mapFragmentBinding.mapCoordinatorLayout)

            mapFragmentBinding.toolbar.findViewById<ImageButton>(R.id.btn_humberger)
                    .setImageResource(R.drawable.ic_close_black_24dp)
            viewModel.uiState.isSearchOpen = true
        }

    }

    private fun onCloseSearch(){
        if (viewModel.uiState.isSearchOpen) {
            YoYo.with(Techniques.SlideOutUp)
                    .duration(500)
                    .delay(100)
                    .repeat(0)
//                    .onEnd {
//                        search_results.visibility = View.GONE
//                    }
                    .playOn(mapFragmentBinding.searchResults)


            YoYo.with(Techniques.FadeIn)
                    .duration(500)
                    .repeat(0)
//                    .onStart{
//                        details_frame.visibility = View.VISIBLE
//                    }
                    .playOn(mapFragmentBinding.mapCoordinatorLayout)

            mapFragmentBinding.toolbar.findViewById<ImageButton>(R.id.btn_humberger)
                    .setImageResource(R.drawable.ic_humberger)
            viewModel.uiState.isSearchOpen = false
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMapClickListener {
            behavior.isHideable = true
            behavior.state = GoogleMapsBottomSheetBehavior.STATE_HIDDEN
        }

        if (permissionsGranted) {
            map.isMyLocationEnabled = true
        }
        setupMapAdapter()
    }

    private fun setupMapAdapter(){
        mapsAdapter.attach(mapFragmentBinding.root, map)

        mapsAdapter.setOnAnnotationClickListener(OnMapAnnotationClickListener {
            onGymClicked(viewModel.gyms[it.position])
            true
        })

        mapsAdapter.setOnInfoWindowClickListener(OnInfoWindowClickListener {
            onGymClicked(viewModel.gyms[it.position])
            true
        })

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

    fun centerOnUser(latLng: LatLng){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))
        map.addCircle(
                CircleOptions()
                        .center(latLng)
                        .radius(viewModel.uiState.nearbyDistance * 1000.0)
                        .strokeWidth(1f)
                        .strokeColor(resources.getColor(R.color.colorPrimary))
                        .fillColor(resources.getColor(R.color.translucentColorPrimary))
        )
    }

    fun moveCamera(latLng: LatLng, zoom: Float, mark: Boolean = false, markerTitle: String = ""){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom))
        if (mark) {
            map.addMarker(
                    MarkerOptions()
                            .title(markerTitle)
                            .snippet("lat:${latLng.latitude}, lng:${latLng.longitude}")
                            .position(latLng)
            )
        }
    }

    private fun hideSoftKeyboard(){
        val imm : InputMethodManager = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view : View?  = activity?.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setupDistanceEntry(){
        customDistanceEntryBinging = DataBindingUtil.inflate(layoutInflater,
                R.layout.custom_distance_entry_layout, null, false)
        customDistanceEntryBinging.viewModel = viewModel

        customDistanceEntryDialog = MaterialDialog.Builder(context!!)
                .customView(customDistanceEntryBinging.root, true)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive { _, _ ->
                    presenter.refetchNearbyGyms()
                }
                .build()
    }

    override fun onGymClicked(gym: Gym) {
        hideSoftKeyboard()
        onCloseSearch()
        (childFragmentManager.findFragmentById(R.id.gym_view) as GymViewFragment).onGymClicked(gym)
        mapFragmentBinding.tvName.text = gym.name
        mapFragmentBinding.tvLocation.text = gym.location?.address
        mapFragmentBinding.tvToolbarName.text = gym.name
        mapFragmentBinding.ratingAverage.text = gym.rating.toString()
        mapFragmentBinding.ratingBar.rating = gym.rating
        mapFragmentBinding.ratingUsersCount.text = gym.rating_user_count.toString()

        gym.location?.latLng?.split(",")?.apply {
            if(!viewModel.gyms.contains(gym))
                moveCamera(LatLng(this[0].toDouble(),this[1].toDouble()),15f,true,gym.name)
            else moveCamera(LatLng(this[0].toDouble(),this[1].toDouble()),15f)
        }

        if (!gym.pictures.isEmpty())
            Picasso.get()
                    .load(gym.pictures[0].remote_location)
                    .placeholder(R.drawable.image_placeholder)
                    .into(mapFragmentBinding.imPicture)
        else mapFragmentBinding.imPicture.setImageDrawable(
                TextDrawable.builder().buildRound(
                        gym.name.toCharArray()[0].toString(),
                        ColorGenerator.MATERIAL.getColor(gym.name)
                )
        )

        behavior.state = GoogleMapsBottomSheetBehavior.STATE_COLLAPSED
        behavior.isHideable = false
    }

    override fun onError(error: String) {
        showError(error)
    }

    override fun onSuccess(message: String) {
        showMessage(message)
    }

    override fun onFinish(success: Boolean, data: Bundle) {
//        presenter.onLocationUpdate(data)
    }

    override fun onUpdateUI(data: Bundle) {
        presenter.onLocationUpdate(data)
    }

    interface BottomSheetStateListener{
        fun onStateChanged(newState:Int)
    }
}
