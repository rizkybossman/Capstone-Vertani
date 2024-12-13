package com.dicoding.vertani.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ElevationService {
    private val api: ElevationApi

    init {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        api = retrofit.create(ElevationApi::class.java)
    }

    suspend fun fetchElevation(latitude: Double, longitude: Double): List<Float> {
        return try {
            val response = api.getElevation(latitude, longitude)
            response.elevation.map { it as Float }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

}


