package com.canonicalexamples.mypantry.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "food_table")
data class Food(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val quantity: Int = -1,
    val calories: Int = -1,
    val calores: Int = -1
    ) {
    val isValid: Boolean
        get() = name.isNotEmpty() && 0 <= quantity
}
