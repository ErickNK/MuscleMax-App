package com.flycode.musclemax_app.data.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.musclemax_app.data.db.Database
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

@Table(database = (Database::class), name = "pictures" )
class Picture: BaseObservable(){
    @field: [PrimaryKey Column(name = "id") SerializedName("id")]
    var id : Int = 0

    @field: [SerializedName("picturable_id") Column(name = "picturable_id")]
    var picturable_id : Int = 0

    @field: [SerializedName("picturable_type") Column(name = "picturable_type")]
    var picturable_type : String = ""

    @field: [SerializedName("name") Column(name = "name")]
    @get: Bindable
    var name: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("type") Column(name = "type")]
    @get: Bindable
    var type: String = ""
        set(value) {
            field = value
            notifyChange()
        }


    @field: [SerializedName("size") Column(name = "size")]
    @get: Bindable
    var size: Int = 0
        set(value) {
            field = value
            notifyChange()
        }


    @field: [SerializedName("remote_location") Column(name = "remote_location")]
    @get: Bindable
    var remote_location: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("local_location") Column(name = "local_location")]
    @get: Bindable
    var local_location: String = ""
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
}