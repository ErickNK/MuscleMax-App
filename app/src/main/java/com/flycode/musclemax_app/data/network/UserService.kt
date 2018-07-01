package com.flycode.musclemax_app.data.network

import com.flycode.musclemax_app.data.models.Response
import com.flycode.musclemax_app.data.models.User
import io.reactivex.Observable
import retrofit2.http.*

interface UserService{

    @GET("api/{model}/getAll")
    fun getAll(
            @Path("model") model :String
    ): Observable<Response<List<User>>>

    @GET("api/{model}/get/{id}")
    fun get(
            @Path("model") model : String,
            @Path("id") id : Int
    ): Observable<Response<User>>

    @POST("api/{model}/add")
    fun add(
            @Path("model") model : String,
            @Body user : User

    ): Observable<Response<User>>

    @DELETE("api/{model}/{id}")
    fun delete(
            @Path("model") model : String,
            @Path("id") id : Int
    ): Observable<Response<User>>

    @PUT("api/{model}/{id}")
    fun update(
            @Path("model") model : String,
            @Path("id") id : Int
    ): Observable<Response<User>>

    @POST("api/{model}/add")
    fun addWithPictures(
            @Path("model") model : String,
            @Body user : User,
            @Query("with_temp_pics") withPics : Boolean = true

    ): Observable<Response<User>>
}