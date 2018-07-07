package com.flycode.musclemax_app.data.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import java.io.Serializable

class Credentials: BaseObservable(), Serializable {
    @get: Bindable
    var email: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @get: Bindable
    var password: String = ""
        set(value) {
            field = value
            notifyChange()
        }
}

