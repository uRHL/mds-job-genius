package com.canonicalexamples.jobgenius.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "job_table")
data class Job(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val githubId: String = "",
        val title: String = "",
        val fav: Boolean = false,
        val remote: Boolean = false,
        val company: String = "",
        val location: String = "",
        val description: String = "",
        val imageUrl: String = "",
        val url: String = ""
    ) {
    val isValid: Boolean
        get() = title.isNotEmpty()
}
