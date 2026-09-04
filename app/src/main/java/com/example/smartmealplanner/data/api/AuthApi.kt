package com.example.smartmealplanner.data.api

import com.example.smartmealplanner.data.model.AuthResponse
import com.example.smartmealplanner.data.model.LoginRequest
import com.example.smartmealplanner.data.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
}