package com.flycode.musclemax_app.ui.main.map

import android.content.Context
import android.graphics.BitmapFactory
import com.flycode.musclemax_app.R
import com.flycode.musclemax_app.data.models.Gym
import com.google.android.gms.maps.GoogleMap
import nz.co.trademe.mapme.LatLng
import nz.co.trademe.mapme.annotations.AnnotationFactory
import nz.co.trademe.mapme.annotations.MapAnnotation
import nz.co.trademe.mapme.annotations.MarkerAnnotation
import nz.co.trademe.mapme.googlemaps.GoogleMapMeAdapter

class MapsAdapter(
        context: Context
) : GoogleMapMeAdapter(context) {

    var markers: List<Gym> = ArrayList()

    override fun getItemCount(): Int {
        return markers.size
    }

    override fun onBindAnnotation(annotation: MapAnnotation, position: Int, payload: Any?) {
        if (annotation is MarkerAnnotation) {
            val item = this.markers[position]
            annotation.title = item.name
        }
    }

    override fun onCreateAnnotation(factory: AnnotationFactory<GoogleMap>, position: Int, annotationType: Int): MapAnnotation {
        val item = this.markers[position]
        val latlng  = item.location?.latLng?.trim()?.split(",")

        return if (latlng != null){
            factory.createMarker(LatLng(latlng[0].toDouble(),latlng[1].toDouble()),
                    BitmapFactory.decodeResource(context.resources,
                            R.drawable.ic_gym_map_pin), item.name)
        }else
            factory.createMarker(LatLng(0.0,0.0),
                    BitmapFactory.decodeResource(context.resources,
                            R.drawable.ic_gym_map_pin), item.name)

    }

}