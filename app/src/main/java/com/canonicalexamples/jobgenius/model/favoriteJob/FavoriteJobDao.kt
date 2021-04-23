package com.canonicalexamples.jobgenius.model.favoriteJob

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteJobDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addJob(fabJob: FavoriteJob)

    @Query("SELECT * FROM fab_job_table WHERE id = :id")
    fun get(id: Int): FavoriteJob?

    @Query("SELECT * FROM fab_job_table")
    fun fetchJobs(): LiveData<List<FavoriteJob>>

    @Update
    suspend fun update(fabJob: FavoriteJob)

    @Query("DELETE FROM fab_job_table WHERE pk = :id")
    suspend fun delete(id: Int)
}
