package com.canonicalexamples.jobgenius.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.canonicalexamples.jobgenius.model.favoriteJob.FavoriteJob
import com.canonicalexamples.jobgenius.model.favoriteJob.FavoriteJobDao
import com.canonicalexamples.jobgenius.model.job.Job
import com.canonicalexamples.jobgenius.model.job.JobDao


@Database(entities = [Job::class, FavoriteJob::class], version = 6, exportSchema = false)
abstract class JobDatabase: RoomDatabase() {
    abstract fun jobDao(): JobDao
    abstract fun favoriteJobDao(): FavoriteJobDao

    companion object {
        @Volatile
        private var INSTANCE: JobDatabase? = null

        fun getInstance(context: Context): JobDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JobDatabase::class.java,
                    "github_offers_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
