package com.canonicalexamples.jobgenius.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface JobDao {
    @Insert
    suspend fun create(job: Job)
    @Query("SELECT * FROM job_table WHERE id = :id")
    suspend fun get(id: Int): Job?
    @Query("SELECT * FROM job_table")
    suspend fun fetchJob(): List<Job>
    @Update
    suspend fun update(job: Job)
    @Query("DELETE FROM job_table WHERE id = :id")
    suspend fun delete(id: Int)
}
