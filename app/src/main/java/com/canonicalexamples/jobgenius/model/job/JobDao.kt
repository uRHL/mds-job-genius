package com.canonicalexamples.jobgenius.model.job

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface JobDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addJob(job: Job)

    @Query("SELECT * FROM job_table WHERE id = :id")
    fun get(id: Int): Job?

    @Query("SELECT * FROM job_table")
    fun fetchJobs(): LiveData<List<Job>>

    @Update
    suspend fun update(job: Job)

    @Query("DELETE FROM job_table WHERE id = :id")
    suspend fun delete(id: Int)
}
