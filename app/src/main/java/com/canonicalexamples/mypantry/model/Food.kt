package com.canonicalexamples.mypantry.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "food_table")
data class Food(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val rating: Int = -1
    ) {
    val isValid: Boolean
        get() = name.isNotEmpty() && id >= 0 && rating>=0 && rating < 5
}
