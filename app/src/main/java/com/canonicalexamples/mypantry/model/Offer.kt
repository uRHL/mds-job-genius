package com.canonicalexamples.mypantry.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "offer_table")
data class Offer(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val quantity: Int = -1,
    val title: String = "",
    val company: String = "",
    val location: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val url: String = ""
    ) {
    val isValid: Boolean
        get() = name.isNotEmpty() && 0 <= quantity
}
