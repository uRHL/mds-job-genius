package com.canonicalexamples.jobgenius.model.favoriteJob

import androidx.lifecycle.LiveData

class FavoriteJobRepository(private val fabJobDao: FavoriteJobDao) {

    val fetchFabJobs: LiveData<List<FavoriteJob>> = fabJobDao.fetchJobs()

    suspend fun addFabJob(fabJob: FavoriteJob){
        fabJobDao.addJob(fabJob)
    }

    suspend fun removeFabJob(id: Int){
        fabJobDao.delete(id)
    }
}