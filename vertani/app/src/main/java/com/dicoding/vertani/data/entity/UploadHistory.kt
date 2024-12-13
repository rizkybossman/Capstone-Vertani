package com.dicoding.vertani.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class UploadHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val userId: String?,
    val prediction: String,
    val soilPercentage: String,
    val elevation: String,
    val recommendedPlants: String,
    val imageUri: String
): Parcelable