package com.example.smartmealplanner.data.repository

import com.example.smartmealplanner.data.api.AuthApi
import com.example.smartmealplanner.data.model.AuthResponse
import com.example.smartmealplanner.data.model.LoginRequest
import com.example.smartmealplanner.data.model.RegisterRequest

class AuthRepository(private val api: AuthApi) {

    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(name: String, email: String, password: String): Result<AuthResponse> {
        return try {
            val response = api.register(RegisterRequest(name, email, password))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Register failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}