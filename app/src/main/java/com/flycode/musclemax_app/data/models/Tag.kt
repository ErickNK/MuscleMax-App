package com.flycode.musclemax_app.data.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.musclemax_app.data.db.Database
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import java.io.Serializable

@Table(database = Database::class, name = "tags")
class Tag : BaseObservable(), Serializable {
    @field: [PrimaryKey Column(name = "id") SerializedName("id")]
    var id : Int = 0

    @field: [SerializedName("owner_id") Column(name = "owner_id")]
    var owner_id: Int = 0

    @field: [SerializedName("name") Column(name = "name")]
    @get: Bindable
    var name: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("description") Column(name = "description")]
    @get: Bindable
    var description: String = ""
        set(value) {
            field = value
            notifyChange()
        }


    @field: [SerializedName("color") Column(name = "color")]
    @get: Bindable
    var color: Int = 0
        set(value) {
            field = value
            notifyChange()
        }
}
