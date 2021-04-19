package com.canonicalexamples.jobgenius.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
        @PrimaryKey(autoGenerate = true)
        val pk: Int,
        val nameIV : String = "",
        val nameEncodedText : String = "",
        val mailIV : String = "",
        val mailEncodedText : String = "",
        val imageIV : String = "",
        val imageEncodedText : String = ""
)