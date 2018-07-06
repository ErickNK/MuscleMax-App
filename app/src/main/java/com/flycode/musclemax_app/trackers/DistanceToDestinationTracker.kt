package com.flycode.musclemax_app.trackers

import android.arch.lifecycle.LifecycleObserver
import android.content.Context
import com.flycode.musclemax_app.R
import com.google.android.gms.maps.GoogleMap
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.flycode.musclemax_app.data.network.GoogleMapsService
import com.flycode.musclemax_app.ui.base.BaseService
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.internal.disposables.ArrayCompositeDisposable

class DistanceToDestinationTracker(
        val context: Context,
        val googleMapsService: GoogleMapsService,
        val compositeDisposable: CompositeDisposable
) : LifecycleObserver{
    var origin: LatLng? = null
    var dest: LatLng? = null
    var mode: String = "walking"
    var units: String = "metric"
    var listener : DistanceChangedListener? = null

    private fun getDistance() {
        compositeDisposable.add(
                googleMapsService.getDistanceAndDuration(
                units,
                "${origin!!.latitude},${origin!!.longitude}",
                "${dest!!.latitude},${dest!!.longitude}",
                mode
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    for (i in 0 until it.routes.size) {
                        val distance = it.routes[i].legs[i].distance?.value
                        val polyline = decodePoly(it.routes[0].overviewPolyline?.points!!)
                        listener?.onDistanceChanged(distance!!, polyline)
                    }
                },{
                    if (it.message != null){
                        listener?.onTrackerError(it.message!!)
                    }else{
                        listener?.onTrackerError(context.getString(R.string.something_went_wrong))
                    }
                })
        )
    }

    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(lat.toDouble() / 1E5,
                    lng.toDouble() / 1E5)
            poly.add(p)
        }

        return poly
    }

    interface DistanceChangedListener {
        fun onDistanceChanged(distance :Int,polyline: List<LatLng>)
        fun onTrackerError(message: String)
    }
}


