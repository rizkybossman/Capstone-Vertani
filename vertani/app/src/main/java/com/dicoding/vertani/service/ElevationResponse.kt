package com.dicoding.vertani.service

import com.google.gson.annotations.SerializedName

data class ElevationResponse(

    @field:SerializedName("elevation")
    val elevation: List<Float>
)