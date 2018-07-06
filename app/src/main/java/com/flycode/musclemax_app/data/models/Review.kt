package com.flycode.musclemax_app.data.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.musclemax_app.data.db.Database
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.ForeignKey
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

@Table(database = (Database::class), name = "reviews" )
class Review : BaseObservable(){
    @field: [PrimaryKey Column(name = "id") SerializedName("id")]
    var id : Int = 0

//    @field: [SerializedName("owner_id") Column(name = "owner_id")]
//    var owner_id: Int = 0

    @field: [SerializedName("reviewable_id") Column(name = "reviewable_id")]
    var reviewable_id : Int = 0

    @field: [SerializedName("reviewable_type") Column(name = "reviewable_type")]
    var reviewable_type : String = ""

    @field: [SerializedName("content") Column(name = "content")]
    @get: Bindable
    var content: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @ForeignKey
    var owner: User? = null
}