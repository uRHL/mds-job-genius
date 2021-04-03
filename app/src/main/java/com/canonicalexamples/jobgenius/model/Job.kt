package com.canonicalexamples.jobgenius.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "job_table")
data class Job(
        @PrimaryKey(autoGenerate = true)
        val pk: Int,
        // our parameters
        val fav: Boolean = false,

        // github jobs api fields
        var id: String = "",
        var type: String = "",
        var url: String = "",
        var created_at: String = "",
        var company: String = "",
        var company_url: String = "",
        var location: String = "",
        var title: String = "",
        var description: String = "",
        var how_to_apply: String = "",
        var company_logo: String = ""
    ) {
    val isValid: Boolean
        get() = title.isNotEmpty()
}
