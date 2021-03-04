package com.canonicalexamples.mypantry.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface FoodDao {
    @Insert
    suspend fun create(food: Food)
    @Query("SELECT * FROM food_table WHERE id = :id")
    suspend fun get(id: Int): Food?
    @Query("SELECT * FROM food_table")
    suspend fun fetchFoods(): List<Food>
    @Update
    suspend fun update(food: Food)
    @Query("DELETE FROM food_table WHERE id = :id")
    suspend fun delete(id: Int)
}
