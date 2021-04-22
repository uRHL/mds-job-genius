package com.canonicalexamples.jobgenius.model.job

import androidx.lifecycle.LiveData

class JobRepository(private val jobDao: JobDao) {

    val fetchJobs: LiveData<List<Job>> = jobDao.fetchJobs()

    suspend fun addJob(job: Job){
        jobDao.addJob(job)
    }
}