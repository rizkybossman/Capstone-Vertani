package com.dicoding.vertani.data.retrofit

import com.dicoding.vertani.data.response.UploadResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("/pred")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): UploadResponse
}