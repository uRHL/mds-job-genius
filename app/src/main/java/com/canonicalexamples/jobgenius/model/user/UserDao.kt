package com.canonicalexamples.jobgenius.model.user

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM user_table WHERE pk = :id")
    fun get(id: Int): User?

    @Query("SELECT * FROM user_table")
    fun fetchUsers(): LiveData<List<User>>

    @Update
    suspend fun update(user: User)

    @Query("DELETE FROM user_table WHERE pk = :id")
    suspend fun delete(id: Int)
}
