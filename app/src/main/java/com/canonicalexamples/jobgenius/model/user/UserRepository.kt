package com.canonicalexamples.jobgenius.model.user

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {

    val fetchUsers: LiveData<List<User>> = userDao.fetchUsers()

    suspend fun addUser(user: User){
        userDao.addUser(user)
    }
}