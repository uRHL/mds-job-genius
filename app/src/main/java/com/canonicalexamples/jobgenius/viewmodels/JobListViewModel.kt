package com.canonicalexamples.jobgenius.viewmodels

import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
    suspend fun saveFavJob(n: Int): Int {
        return try {
            // Insert into the database
            insertFavoriteJob(oauth.currentUser.uid, jobList.value!![n])
            1 // Transaction successful
        }catch (e: Exception){
            -1 // Transaction failed
        }
    }

    /**
     * @return Positive integer when successful, negative Integer otherwise
     */
    suspend fun removeFavJob(n: Int ): Int {
        val uid = oauth.currentUser.uid
        return try {
            // Remove the selected job from favorites table
            database.favoriteJobDao().delete(fabJobList.value!![n].pk)
            1 // Transaction successful
        }catch (e: Exception){
            -1 // Transaction failed
        }
    }


    fun isUserLogged(): Boolean {
        return oauth.currentUser != null
    }

    fun getCurrentUserUid(): String {
        return if(isUserLogged()) {
            oauth.currentUser.uid
        } else ""
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

    private fun getLoggedUserFavorites(loggedUser: String?):  LiveData<List<FavoriteJob>> {
        lateinit var loggedUserFabJobs: LiveData<List<FavoriteJob>>

        if (loggedUser == null){
            Log.d("JobLisViewModel","No user currently logged")
        }else {
            val iter = this.fabJobList.value!!.listIterator()
            while (iter.hasNext()){
                val elem = iter.next()
                val uid = secureStorage.decryptData(Base64.decode(elem.ownerIV, Base64.DEFAULT), Base64.decode(elem.ownerEncodedText, Base64.DEFAULT))
                if (uid == loggedUser){
                    loggedUserFabJobs.value!!.plusElement(elem)
                }
            }
        }

        return loggedUserFabJobs
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
