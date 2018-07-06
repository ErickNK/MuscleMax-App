package com.flycode.musclemax_app.data.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.musclemax_app.data.db.Database
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.ForeignKey
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

@Table(database = Database::class, name = "notes")
class Note : BaseObservable() {
    @field: [PrimaryKey Column(name = "id") SerializedName("id")]
    var id : Int = 0

    @field: [SerializedName("content") Column(name = "content")]
    @get: Bindable
    var content: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @ForeignKey()
    var weightMeasurement: WeightMeasurement? = null
}
