package com.example.smartmealplanner.data.api

import com.example.smartmealplanner.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // SECURITY: BASE_URL is read from local.properties via BuildConfig
    private val BASE_URL: String = BuildConfig.BASE_URL

    private val logging = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BASIC
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    private fun getClient(tokenManager: TokenManager? = null): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logging)
        
        tokenManager?.let {
            builder.addInterceptor(AuthInterceptor(it))
        }
        
        return builder.build()
    }

    fun <T> create(service: Class<T>, tokenManager: TokenManager? = null): T {
        val okHttpClient = getClient(tokenManager)
        
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(service)
    }

    val instance: ApiService by lazy {
        create(ApiService::class.java)
    }
}
