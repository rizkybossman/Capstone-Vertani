package com.dicoding.vertani.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dicoding.vertani.data.entity.UploadHistory

@Dao
interface UploadHistoryDao {
    @Insert
    suspend fun insert(uploadHistory: UploadHistory)

    @Query("SELECT * FROM UploadHistory WHERE userId = :userId")
    suspend fun getUploadHistoryByUserId(userId: String): List<UploadHistory>

    @Query("SELECT * FROM UploadHistory")
    suspend fun getAllUploadHistory(): List<UploadHistory>
}