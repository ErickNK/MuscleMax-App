package com.flycode.musclemax_app.data.network

import com.flycode.musclemax_app.data.models.Credentials
import com.flycode.musclemax_app.data.models.LoginPayload
import com.flycode.musclemax_app.data.models.Response
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {

    @POST("api/user/login")
    fun login(
            @Body credentials: Credentials
    ) : Observable<Response<LoginPayload>>

    @GET("api/user/logout")
    fun logout()
}