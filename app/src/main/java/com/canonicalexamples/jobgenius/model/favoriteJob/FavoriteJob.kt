package com.canonicalexamples.jobgenius.model.favoriteJob

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.canonicalexamples.jobgenius.model.job.Job


@Entity(tableName = "fab_job_table")
data class FavoriteJob(
        @PrimaryKey(autoGenerate = true)
        val pk: Int,
        // our parameters
        val ownerIV: String = "",
        val ownerEncodedText: String = "",
        val statusIV: String = "",
        val statusEncodedText: String = "",

        // favorite jobs fields
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

    companion object{

        fun parseJob(job: Job): FavoriteJob {
            return FavoriteJob(0, "","","","",job.id, job.type, job.url, job.created_at, job.company, job.company_url, job.location, job.title, job.description, job.how_to_apply, job.company_logo )
        }
    }

}
