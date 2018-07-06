package com.flycode.musclemax_app.service.nearbyGymService

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.rx2.Rx2Apollo
import com.flycode.musclemax_app.SearchNearbyGymsQuery
import com.flycode.musclemax_app.data.models.Gym
import com.flycode.musclemax_app.trackers.GpsTracker
import com.flycode.musclemax_app.ui.base.BaseServicePresenter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NearbyGymPresenter<V : NearbyGymContract.NearbyGymService>(
        val apolloClient: ApolloClient,
        val gpsTracker: GpsTracker
) : BaseServicePresenter<V>(), NearbyGymContract.NearbyGymServicePresenter<V>  {

    private fun getNearbyGymsByDistance(latLng: LatLng): ApolloQueryCall<SearchNearbyGymsQuery.Data> {
        return apolloClient.query(
            SearchNearbyGymsQuery.builder()
                    .raw(Gson().toJson("""
                        {
                            "body":{
                                "query": {
                                    "geo_distance" : {
                                        "distance" : ${service?.nearbyDistance}km,
                                        "latLng" : "${latLng.latitude}, ${latLng.longitude}"
                                    }
                                }
                            }
                        }
                    """.trimIndent()))
                    .build()
        )
    }

    private fun fetchNearbyGyms(latLng: LatLng){
        service?.let {service ->
            compositeDisposable.add(
                    Rx2Apollo.from(this.getNearbyGymsByDistance(latLng))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe ({
                                it.data()?.searchGymLocation()?.hits()?.let {
//                                    service.onUpdateUI(it.toString())
                                }
                            },{
                                if (it.message != null){
                                    service.onError(it.message.toString())
                                }else{
                                    service.onError("Something went wrong. Please try again.")
                                }
                            })
            )
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun init(){
        //Try getting last known location
//        val latLng =  gpsTracker.lastKnownLocation

    }

    override fun onLocationChanged(latLng: LatLng) {
//        fetchNearbyGyms(latLng)
        service?.onUpdateUI(latLng)
    }

    override fun onNearbyDistanceChanged() {
        //TRY GETTING CURRENT LOCATION
        val latLng =  gpsTracker.lastKnownLocation

        if (latLng != null){
            service?.onUpdateUI(latLng)
            service?.isWaitingForNearbyGyms = false
        }else{
            service?.isWaitingForNearbyGyms = true
        }
    }
}