package com.dicoding.vertani.data.response

import com.google.gson.annotations.SerializedName

data class UploadResponse(

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("prediction")
	val prediction: String? = null,

	@field:SerializedName("soil_percentage")
	val soilPercentage: String? = null
)
