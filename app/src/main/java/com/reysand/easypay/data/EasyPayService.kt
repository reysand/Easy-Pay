package com.reysand.easypay.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface EasyPayService {

    @Headers("app-key: 12345", "v: 1")
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @Headers("app-key: 12345", "v: 1")
    @GET("payments")
    suspend fun getPayments(@Header("token") token: String): Response<PaymentListResponse>
}