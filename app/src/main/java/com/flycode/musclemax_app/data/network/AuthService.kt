package com.flycode.musclemax_app.data.network

import com.flycode.musclemax_app.data.models.LoginPayload
import com.flycode.musclemax_app.data.models.Response
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {

    @GET("api/auth/logout")
    fun logout(): Observable<ResponseBody>

    @POST("api/auth/signInWithGoogle")
    fun signInWithGoogle(
            @Body loginPayload: LoginPayload
    ): Observable<Response<LoginPayload>>

    @POST("api/auth/signInWithFacebook")
    fun signInWithFacebook(
            @Body loginPayload: LoginPayload
    ): Observable<Response<LoginPayload>>
}