package com.flycode.musclemax_app.ui.main.map

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.rx2.Rx2Apollo
import com.flycode.musclemax_app.SearchGymsQuery
import com.flycode.musclemax_app.SearchNearbyGymsQuery
import com.flycode.musclemax_app.broadcastReceivers.ServiceStartedReceiver
import com.flycode.musclemax_app.data.models.Gym
import com.flycode.musclemax_app.service.nearbyGymService.NearbyGymService
import com.flycode.musclemax_app.ui.base.BasePresenter
import com.flycode.musclemax_app.ui.flexibleItems.GymListItem
import com.flycode.musclemax_app.ui.flexibleItems.SearchResultsHeaderItem
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.ISectionable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MapPresenter(
        val apolloClient: ApolloClient,
        val mapsAdapter: MapsAdapter,
        val searchListAdapter: FlexibleAdapter<SearchResultsHeaderItem>
): BasePresenter<MapFragment, MapPresenter, MapViewModel>(),
        MapContract.MapPresenter<MapFragment> {
    private lateinit var serviceStartedReceiver: ServiceStartedReceiver
    private lateinit var NearbyGymConnection: ServiceConnection
    private var nearbyGymService : NearbyGymService? = null

    private var isBound: Boolean = false

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    override fun init() {
        serviceStartedReceiver = ServiceStartedReceiver().apply {
            this.listener = object : ServiceStartedReceiver.OnServiceStartedListener {
                override fun onServiceStarted() {
                    syncWithNearbyGymService()
                }
            }
        }

        NearbyGymConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                nearbyGymService = (service as NearbyGymService.NearbyGymServiceBinder).service
                nearbyGymService?.listeners?.add(view!!)
                nearbyGymService?.lastKnownLocation?.let {
                    view?.moveCamera(it,3f)
                    fetchNearbyGyms(it)
                }
                isBound = true
            }

            override fun onServiceDisconnected(name: ComponentName) {
                //                pause();
            }
        }

        startNearbyGymService()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    override fun registerNearbyGymServiceStartedBroadcast() {
        view?.activity?.registerReceiver(serviceStartedReceiver, IntentFilter(NearbyGymService.ON_START_BROADCAST))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    override fun unRegisterNearbyGymServiceStartedBroadcast() {
        view?.activity?.unregisterReceiver(serviceStartedReceiver)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun syncWithNearbyGymService() {
        if (NearbyGymService.isInstanceCreated) /*SERVICE IS RUNNING*/
            view?.activity?.bindService(
                    Intent(view?.context, NearbyGymService::class.java),
                    NearbyGymConnection,
                    0
            )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    override fun unSyncWithNearbyGymService() {
        if (isBound)
            view?.activity?.unbindService(NearbyGymConnection)
        nearbyGymService = null
    }

    override fun startNearbyGymService(){
        if (!isBound) {
            if (view?.activity?.startService(Intent(view?.context, NearbyGymService::class.java))
                    != null) { //If binding is possible
                //Wait for service connection.
            }
            //TODO: better logging
            else view?.showError("Sorry, Something went wrong. Please try again.")
        }
    }

    private fun getNearbyGymsByDistance(latLng: LatLng): ApolloQueryCall<SearchNearbyGymsQuery.Data> {
        return apolloClient.query(
                SearchNearbyGymsQuery.builder()
                        .raw("""
                            {
                                "body":{
                                    "query": {
                                        "geo_distance" : {
                                            "distance" : "${viewModel.nearbyDistance}km",
                                            "latLng" : "${latLng.latitude}, ${latLng.longitude}"
                                        }
                                    }
                                }
                            }
                        """.trimIndent())
                        .build()
        )
    }

    private fun getSearchGymsByName(name: String): ApolloQueryCall<SearchGymsQuery.Data> {
        return apolloClient.query(
                SearchGymsQuery.builder()
                        .raw("""
                            {
                                "body":{
                                    "query": {
                                        "match_phrase" : {
                                            "name" : "$name"
                                        }
                                    }
                                }
                            }
                        """.trimIndent())
                        .build()
        )
    }

    fun searchGyms(name: String) {
        view?.let {view ->
            viewModel.uiState.isSearchLoading = true
            compositeDisposable.add(
                    Rx2Apollo.from(this.getSearchGymsByName(name))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe ({
                                viewModel.searchResultHeaderItem.resultCount = it.data()?.gymComplexSearch()?.totalHits()?.toInt()!!
                                it.data()?.gymComplexSearch()?.hits()?.let {
                                    viewModel.gymSearchResultsList = Gson().fromJson(
                                            Gson().toJson(it),
                                            object : TypeToken<MutableList<Gym>>() {}.type
                                    )

                                    val gymSearchListItems =
                                            ArrayList<GymListItem>().apply {
                                                this.addAll(viewModel.gymSearchResultsList.map {
                                                    GymListItem(viewModel.searchResultHeaderItem,it,context = view.context).apply {
                                                        this.listener = view
                                                    }
                                                })
                                            }

                                    if(!gymSearchListItems.isEmpty()){
                                        viewModel.searchResultHeaderItem.subItems?.clear()
                                        viewModel.searchResultHeaderItem.addSubItems(0,gymSearchListItems as List<ISectionable<*, *>>)
                                        searchListAdapter.notifyDataSetChanged()
                                        searchListAdapter.expandAll()
                                    }
                                    viewModel.uiState.isSearchLoading = false
                                }
                            },{
                                viewModel.uiState.isSearchLoading = false
                                viewModel.uiState.onSearchError = true
                                if (it.message != null){
                                    view.onError(it.message.toString())
                                }else{
                                    view.onError("Something went wrong. Please try again.")
                                }
                            })
            )
        }
    }

    fun refetchNearbyGyms(){
        nearbyGymService?.lastKnownLocation?.let {
            fetchNearbyGyms(it)
        }
    }

    private fun fetchNearbyGyms(latLng: LatLng){
        view?.let {view ->
            viewModel.uiState.isMapLoading = true
            compositeDisposable.add(
                    Rx2Apollo.from(this.getNearbyGymsByDistance(latLng))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe ({
                                it.data()?.searchGymLocation()?.hits()?.let {
                                    viewModel.gyms = Gson().fromJson(
                                            Gson().toJson(it),
                                            object : TypeToken<MutableList<Gym>>() {}.type
                                    )
                                    mapsAdapter.markers = viewModel.gyms
                                    mapsAdapter.notifyDataSetChanged()
                                    viewModel.uiState.isMapLoading = false
                                }
                            },{
                                viewModel.uiState.isMapLoading = false
                                viewModel.uiState.onMapError = true
                                if (it.message != null){
                                    view.onError(it.message.toString())
                                }else{
                                    view.onError("Something went wrong. Please try again.")
                                }
                            })
            )
        }
    }

    override fun onLocationUpdate(data: Bundle){
        fetchNearbyGyms(LatLng(data.getDouble("lat"),data.getDouble("lng")))
    }

    fun onGymClicked(gym: Gym) {

    }

    fun onGymInfoWindowClicked(gym: Gym) {

    }


}