package com.flycode.musclemax_app.data.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.flycode.musclemax_app.data.db.Database
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.sql.language.SQLite
import java.io.Serializable

@Table(database = (Database::class), name = "gyms" )
class Gym : BaseObservable(), Serializable{
    @field: [PrimaryKey Column(name = "id") SerializedName("id")]
    var id : Int = 0

    @field: [SerializedName("name") Column(name = "name")]
    @get: Bindable
    var name: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @field: [SerializedName("helpline") Column(name = "helpline")]
    @get: Bindable
    var helpline: String = ""
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


    // RELATIONSHIPS
    var pictures : MutableList<Picture> = ArrayList()

    var reviews : MutableList<Review> = ArrayList()

    var tags : MutableList<Tag> = ArrayList()

    @ForeignKey(saveForeignKeyModel = true)
    var location: Location? = null

    @OneToMany(methods = [OneToMany.Method.ALL],variableName = "pictures")
    fun getMyPictures() : List<Picture>?{
        if (pictures.isEmpty())
            pictures = SQLite.select()
                    .from(Picture::class.java)
                    .where(Picture_Table.picturable_id.eq(id),Picture_Table.picturable_type.eq("gym"))
                    .queryList()
        return pictures
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