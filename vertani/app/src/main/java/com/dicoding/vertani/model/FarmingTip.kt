
package com.dicoding.vertani.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class FarmingTip(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String? = null,
) : Parcelable
