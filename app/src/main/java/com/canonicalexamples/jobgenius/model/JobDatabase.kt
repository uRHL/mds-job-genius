package com.canonicalexamples.jobgenius.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Job::class], version = 1, exportSchema = false)
abstract class JobDatabase: RoomDatabase() {
    abstract val jobDao: JobDao

    companion object {
        @Volatile
        private var INSTANCE: JobDatabase? = null
        fun getInstance(context: Context): JobDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JobDatabase::class.java,
                    "my_pantry_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
