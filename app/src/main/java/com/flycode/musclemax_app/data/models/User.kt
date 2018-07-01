package com.flycode.musclemax_app.data.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.musclemax_app.data.db.Database
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.OneToMany
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.sql.language.SQLite

@Table(database = (Database::class), name = "users" )
class User : BaseObservable(){
    @field: [PrimaryKey Column(name = "id") SerializedName("id")]
    var id : Int = 0

    @field: [SerializedName("firstname") Column(name = "firstname")]
    @get: Bindable
    var firstname: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("lastname") Column(name = "lastname")]
    @get: Bindable
    var lastname: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("email") Column(name = "email")]
    @get: Bindable
    var email: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("password") Column(name = "password")]
    @get: Bindable
    var password: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    // RELATIONSHIPS
    var pictures : MutableList<Picture>? = ArrayList()

    @OneToMany(methods = [OneToMany.Method.ALL],variableName = "pictures")
    fun getMyTimes() : List<Picture>?{
        if (pictures == null)
            pictures = SQLite.select()
                    .from(Picture::class.java)
                    .where(Picture_Table.picturable_id.eq(id),Picture_Table.picturable_type.eq("userPic"))
                    .queryList()
        return pictures
    }
}