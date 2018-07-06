package com.flycode.musclemax_app.data.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.musclemax_app.data.db.Database
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.sql.language.SQLite
import java.util.*


/**
 * User model used to move and persist data
 * associated with weight entity.
 *
 */
@Table(database = Database::class, name = "weight_measurement")
@ManyToMany(referencedTable = Tag::class)
class WeightMeasurement : BaseObservable() {
    @field: [PrimaryKey Column(name = "id") SerializedName("id")]
    var id : Int = 0

    @field: [SerializedName("owner_id") Column(name = "owner_id")]
    @get: Bindable
    var owner_id: Int = 0
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("weight") Column(name = "weight")]
    @get: Bindable
    var weight: Float = 0f
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("height") Column(name = "height")]
    @get: Bindable
    var height: Float = 0f
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("bmi") Column(name = "bmi")]
    @get: Bindable
    var bmi: Float = 0f
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("type") Column(name = "type")]
    @get: Bindable
    var type: String = "normal"
        set(value) {
            field = value
            notifyChange()
        }


    @field: [SerializedName("created_at") Column(name = "created_at")]
    @get: Bindable
    var created_at: Long = Date().time
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("updated_at") Column(name = "updated_at")]
    @get: Bindable
    var updated_at: Long = Date().time
        set(value) {
            field = value
            notifyChange()
        }


    //RELATIONSHIPS
    @ForeignKey(saveForeignKeyModel = true)
    var note: Note? = null

    var pictures : MutableList<Picture> = ArrayList()

    @OneToMany(methods = [OneToMany.Method.ALL],variableName = "pictures")
    fun getMyTimes() : List<Picture>?{
        if (pictures.isEmpty())
            pictures = SQLite.select()
                    .from(Picture::class.java)
                    .where(Picture_Table.picturable_id.eq(id),Picture_Table.picturable_type.eq("weight_measurement"))
                    .queryList()
        return pictures
    }
}
