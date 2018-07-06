package com.flycode.musclemax_app.data.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.musclemax_app.data.db.Database
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.sql.language.SQLite
import java.io.Serializable

@Table(database = (Database::class), name = "users" )
class User : BaseObservable(),Serializable{
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

    @field: [SerializedName("age") Column(name = "age")]
    @get: Bindable
    var age: Int = 0
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

    @field: [SerializedName("gender") Column(name = "gender")]
    @get: Bindable
    var gender: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("tel") Column(name = "tel")]
    @get: Bindable
    var tel: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("_tag") Column(name = "_tag")]
    @get: Bindable
    var _tag: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("rating") Column(name = "rating")]
    @get: Bindable
    var rating: Float = 0f
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("rating_user_count") Column(name = "rating_user_count")]
    @get: Bindable
    var rating_user_count: Int = 0
        set(value) {
            field = value
            notifyChange()
        }

    var reviews : MutableList<Review> = ArrayList()

    var tags : MutableList<Tag> = ArrayList()

    // RELATIONSHIPS
    var pictures : MutableList<Picture> = ArrayList()

    @ForeignKey(saveForeignKeyModel = true)
    var location : Location? = null

    var weight_measurements: MutableList<WeightMeasurement> = ArrayList()

    @OneToMany(methods = [OneToMany.Method.ALL],variableName = "pictures")
    fun getMyTimes() : List<Picture>?{
        if (pictures.isEmpty())
            pictures = SQLite.select()
                    .from(Picture::class.java)
                    .where(Picture_Table.picturable_id.eq(id),Picture_Table.picturable_type.eq("user"))
                    .queryList()
        return pictures
    }

    @OneToMany(methods = [OneToMany.Method.ALL],variableName = "weight_measurements")
    fun getMyWeightMeasureMents() : List<WeightMeasurement>?{
        if (weight_measurements.isEmpty())
            weight_measurements = SQLite.select()
                    .from(WeightMeasurement::class.java)
                    .where(WeightMeasurement_Table.owner_id.eq(id))
                    .queryList()
        return weight_measurements
    }

    @OneToMany(methods = [OneToMany.Method.ALL],variableName = "reviews")
    fun getMyReviews() : List<Review>?{
        if (reviews.isEmpty())
            reviews = SQLite.select()
                    .from(Review::class.java)
                    .where(Review_Table.reviewable_id.eq(id),Review_Table.reviewable_type.eq("gym"))
                    .queryList()
        return reviews
    }
}