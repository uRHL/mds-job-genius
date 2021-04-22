package com.canonicalexamples.jobgenius.viewmodels

import android.util.Base64
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.canonicalexamples.jobgenius.R
import com.canonicalexamples.jobgenius.databinding.JobCardItemBinding
import com.canonicalexamples.jobgenius.model.JobDatabase
import com.canonicalexamples.jobgenius.model.favoriteJob.FavoriteJob
import com.canonicalexamples.jobgenius.model.favoriteJob.FavoriteJobRepository
import com.canonicalexamples.jobgenius.model.job.Job
import com.canonicalexamples.jobgenius.model.job.JobRepository
import com.canonicalexamples.jobgenius.util.SecureStorage
import com.google.firebase.auth.FirebaseAuth


class JobListViewModel(private val database: JobDatabase, private val oauth: FirebaseAuth) : ViewModel() {

    private val jobRepository: JobRepository = JobRepository(database.jobDao())
    private val fabJobRepository: FavoriteJobRepository = FavoriteJobRepository(database.favoriteJobDao())
    val jobList: LiveData<List<Job>> = jobRepository.fetchJobs
    val fabJobList: LiveData<List<FavoriteJob>> = fabJobRepository.fetchFabJobs

    private val secureStorage: SecureStorage = SecureStorage()



    fun onClickJobMore(n: Int) {
        // Navigate to the fragment job details
        println("Item $n clicked")
        println("Some information: title ${jobList.value!![n].title}, description: ${jobList.value!![n].description}")
    }

    /**
     * @return Positive integer when successful, negative Integer otherwise
     */
    suspend fun onClickJobFav(n: Int, fav: Boolean, binding: JobCardItemBinding): Int {
        val user = oauth.currentUser
        return try {
            // If the job is marked as favorite if it was not
            if(!fav){
                // Insert into the database
                val uid: String =  user.uid
                insertFavoriteJob(uid, jobList.value!![n])
            }else{
                // Remove from the database
            }
            1 // Transaction successful
        }catch (e: Exception){
            -1 // Transaction failed
        }
    }

    fun isUserLogged(): Boolean {
        return oauth.currentUser != null
    }

    private suspend fun insertFavoriteJob(uid: String, job: Job) {
        val uidPair = secureStorage.encryptData(uid)
        val statusPair = secureStorage.encryptData("0")

        val uidIv = Base64.encodeToString(uidPair.first, Base64.DEFAULT)
        val uidEncodedText = Base64.encodeToString(uidPair.second, Base64.DEFAULT)
        val statusIv = Base64.encodeToString(statusPair.first, Base64.DEFAULT)
        val statusEncodedText = Base64.encodeToString(statusPair.second, Base64.DEFAULT)
        
        val jobFab = FavoriteJob(
                0,
                uidIv, 
                uidEncodedText, 
                statusIv, 
                statusEncodedText,
                job.id,
                job.type,
                job.url,
                job.created_at,
                job.company,
                job.company_url,
                job.location,
                job.title,
                job.description,
                job.how_to_apply,
                job.company_logo
        )
        database.favoriteJobDao().addJob(jobFab)
    }

}

class JobListViewModelFactory(private val database: JobDatabase, private val oauth: FirebaseAuth) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JobListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JobListViewModel(database, oauth) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
