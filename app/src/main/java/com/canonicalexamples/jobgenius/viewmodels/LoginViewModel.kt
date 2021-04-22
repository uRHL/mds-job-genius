package com.canonicalexamples.jobgenius.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.canonicalexamples.jobgenius.model.*
import com.canonicalexamples.jobgenius.model.user.User
import com.canonicalexamples.jobgenius.model.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.NullPointerException
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class LoginViewModel(private val database: JobDatabase) : ViewModel() {

    private val repository: UserRepository = UserRepository(database.userDao())
    val userList: LiveData<List<User>> = repository.fetchUsers

//    fun encryptData(data: String): Pair<ByteArray, ByteArray> {
//        val cipher: Cipher = Cipher.getInstance("AES/CBC/NoPadding")
//
//        var temp: String = data
//        while (temp.toByteArray().size % 16 != 0)
//            temp += "\u0020"
//
//        cipher.init(Cipher.ENCRYPT_MODE, getKey())
//        val ivBytes = cipher.iv
//        val encryptedBytes = cipher.doFinal(temp.toByteArray())
//
//        return Pair(ivBytes, encryptedBytes)
//    }

    fun addUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }


//    fun checkKey(): Boolean {
//        val keystore: KeyStore = KeyStore.getInstance("AndroidKeyStore")
//        keystore.load(null)
//        val secretKeyEntry: KeyStore.SecretKeyEntry
//        try {
//            secretKeyEntry = keystore.getEntry("MyKeyStore", null) as KeyStore.SecretKeyEntry
//        }catch (e: NullPointerException){
//            // No entry exists in the specified KeyStore
//            return false
//        }
//        return secretKeyEntry.secretKey != null
//    }
//
//    private fun getKey(): SecretKey {
//        val keystore: KeyStore = KeyStore.getInstance("AndroidKeyStore")
//        keystore.load(null)
//        val secretKeyEntry = keystore.getEntry("MyKeyStore", null) as KeyStore.SecretKeyEntry
//        return secretKeyEntry.secretKey
//    }
//
//    fun decryptData(ivBytes: ByteArray, data: ByteArray): String{
//        val cipher = Cipher.getInstance("AES/CBC/NoPadding")
//        val spec = IvParameterSpec(ivBytes)
//        cipher.init(Cipher.DECRYPT_MODE, getKey(), spec)
//        return cipher.doFinal(data).toString(Charsets.UTF_8).trim()
//    }
}

class LoginViewModelFactory(private val database: JobDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}