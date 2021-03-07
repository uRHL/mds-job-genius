package com.canonicalexamples.mypantry.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Offer::class], version = 1, exportSchema = false)
abstract class OfferDatabase: RoomDatabase() {
    abstract val offerDao: OfferDao

    companion object {
        @Volatile
        private var INSTANCE: OfferDatabase? = null
        fun getInstance(context: Context): OfferDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OfferDatabase::class.java,
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
