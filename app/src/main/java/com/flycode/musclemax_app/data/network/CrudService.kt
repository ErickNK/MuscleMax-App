package com.flycode.musclemax_app.data.network

import com.flycode.musclemax_app.data.models.Response
import com.flycode.musclemax_app.data.models.User
import io.reactivex.Observable
import retrofit2.http.*

interface CrudService<D> {

    @GET("api/{model}/getAll")
    fun getAll(
        @Path("model") model :String
    ): Observable<Response<List<D>>>

    @GET("api/{model}/get/{id}")
    fun get(
        @Path("model") model : String,
        @Path("id") id : Int
    ): Observable<Response<D>>

    @POST("api/{model}/add")
    fun add(
        @Path("model") model : String,
        @Body user : User

    ): Observable<Response<D>>

    @DELETE("api/{model}/{id}")
    fun delete(
        @Path("model") model : String,
        @Path("id") id : Int
    ): Observable<Response<D>>

    @PUT("api/{model}/{id}")
    fun update(
        @Path("model") model : String,
        @Path("id") id : Int
    ): Observable<Response<D>>
}