package com.androiddev.loanapplication.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiConfig {

    companion object {
        fun getApiService(): ApiInterface {

            // API response interceptor
            val loggingInterceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)

            // Client
            val client = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)  // Connection timeout
                .readTimeout(10, TimeUnit.SECONDS)     // Read timeout
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build()

            // Retrofit
            val retrofit = Retrofit.Builder()
                .baseUrl("http://98.70.11.251:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiInterface::class.java)
        }

    }
}