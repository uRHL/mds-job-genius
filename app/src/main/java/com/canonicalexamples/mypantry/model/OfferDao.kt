package com.canonicalexamples.mypantry.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface OfferDao {
    @Insert
    suspend fun create(offer: Offer)
    @Query("SELECT * FROM offer_table WHERE id = :id")
    suspend fun get(id: Int): Offer?
    @Query("SELECT * FROM offer_table")
    suspend fun fetchFoods(): List<Offer>
    @Update
    suspend fun update(offer: Offer)
    @Query("DELETE FROM offer_table WHERE id = :id")
    suspend fun delete(id: Int)
}
