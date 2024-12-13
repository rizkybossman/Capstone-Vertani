package com.dicoding.vertani.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.vertani.data.entity.UploadHistory

@Database(
    entities = [UploadHistory::class],
    version = 2)
abstract class UploadHistoryDatabase : RoomDatabase() {
    abstract fun uploadHistoryDao(): UploadHistoryDao

    companion object {
        private var INSTANCE: UploadHistoryDatabase? = null

        fun getInstance(context: Context, userId: String): UploadHistoryDatabase {
            require(userId.isNotEmpty()) { "UserId cannot be empty" }
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    UploadHistoryDatabase::class.java,
                    "upload_history_database_$userId"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }
    }
}