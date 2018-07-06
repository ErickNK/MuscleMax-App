package com.flycode.musclemax_app.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.ArrayList


class GoogleMap {

    inner class Duration {
        @SerializedName("text")
        @Expose
         val text: String? = null
        @SerializedName("value")
        @Expose
         val value: Int? = null
    }

    inner class Distance {
        @SerializedName("text")
        @Expose
         val text: String? = null
        @SerializedName("value")
        @Expose
         val value: Int? = null
    }

    inner class Leg {
        @SerializedName("distance")
        @Expose
         val distance: Distance? = null
        @SerializedName("duration")
        @Expose
         val duration: Duration? = null
    }

    inner class OverviewPolyline {
        @SerializedName("points")
        @Expose
         val points: String? = null
    }

    inner class Route {
        @SerializedName("legs")
        @Expose
         val legs = ArrayList<Leg>()
        @SerializedName("overview_polyline")
        @Expose
         val overviewPolyline: OverviewPolyline? = null
    }

    inner class Routes {
        @SerializedName("routes")
        @Expose
         val routes = ArrayList<Route>()
    }
}
