package com.flycode.musclemax_app.data.network

import android.graphics.Bitmap
import com.flycode.musclemax_app.data.Config
import com.flycode.musclemax_app.data.models.GoogleMap

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMapsService {

    /*
     * Retrofit get annotation with our URL
     * And our method that will return us details of student.
     */
    @GET("api/directions/json")
    fun getDistanceAndDuration(
            @Query("units") units: String,
            @Query("origin") origin: String,
            @Query("destination") destination: String,
            @Query("mode") mode: String,
            @Query("key") key: String = Config.GOOGLE_API_KEY
    ): Observable<GoogleMap.Routes>

}
