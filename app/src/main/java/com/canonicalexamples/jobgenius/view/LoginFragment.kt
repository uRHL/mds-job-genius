package com.canonicalexamples.jobgenius.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.canonicalexamples.jobgenius.R
import com.canonicalexamples.jobgenius.app.JobGeniusApp
import com.canonicalexamples.jobgenius.databinding.FragmentLoginBinding
import com.canonicalexamples.jobgenius.model.User
import com.canonicalexamples.jobgenius.viewmodels.LoginViewModel
import com.canonicalexamples.jobgenius.viewmodels.LoginViewModelFactory
import com.canonicalexamples.jobgenius.viewmodels.SearchViewModel
import com.canonicalexamples.jobgenius.viewmodels.SearchViewModelFactory


class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModels {
        val app = activity?.application as JobGeniusApp
        LoginViewModelFactory(app.database)
    }

    companion object{
        private const val RC_SIGN_IN = 120
    }

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    //val app = activity?.application as JobGeniusApp

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)


        val view = binding.root

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if(user != null){
            findNavController().navigate(R.id.action_LoginFragment_to_SearchFragment)
        }

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        binding.signInBtn.setOnClickListener{
            signIn()
        }

        return view
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("Fragment Login", "firebaseAuthWithGoogle:" + account.id)
                Log.d("****** ", account.email + "\t" + account.displayName + "\t" + account.photoUrl)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("Fragment Login", "Google sign in failed", e)
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Fragment Login", "signInWithCredential:success")
                        val user = auth.currentUser
                        if(user != null){
                            Log.d("****** ", user.email + "\t" + user.photoUrl + "\t" + user.displayName)
                            insertDataToDatabase(user.displayName, user.email, user.photoUrl.toString())
                            findNavController().navigate(R.id.action_LoginFragment_to_SearchFragment)
                        }
                    } else {
                        Log.w("Fragment Login", "signInWithCredential:failure", task.exception)
                    }
                }
    }

    private fun insertDataToDatabase(name: String, mail: String, image: String) {
        val namePair = loginViewModel.encryptData(name)
        val mailPair = loginViewModel.encryptData(mail)
        val imagePair = loginViewModel.encryptData(image)

        val nameIV = Base64.encodeToString(namePair.first, Base64.DEFAULT)
        val nameEncodedText = Base64.encodeToString(namePair.second, Base64.DEFAULT)
        val mailIV = Base64.encodeToString(mailPair.first, Base64.DEFAULT)
        val mailEncodedText = Base64.encodeToString(mailPair.second, Base64.DEFAULT)
        val imageIV = Base64.encodeToString(imagePair.first, Base64.DEFAULT)
        val imageEncodedText = Base64.encodeToString(imagePair.second, Base64.DEFAULT)

        val user = User(0,nameIV,nameEncodedText,mailIV,mailEncodedText,imageIV,imageEncodedText)
//        val user = User(0, namePair, mailPair, imagePair)


//        Log.d("Encrypt", "insertDataToDatabase: $pair")
//        val encodedIV: String = Base64.encodeToString(pair.first, Base64.DEFAULT)
//        val encodedText: String = Base64.encodeToString(pair.second, Base64.DEFAULT)
//        val student = Student(0, firstName, lastName, Integer.parseInt(age.toString()),
//                    encodedIV, encodedText)
        loginViewModel.addUser(user)
        Toast.makeText(requireContext(), "User logged in", Toast.LENGTH_LONG).show()
        //findNavController().navigate(R.id.action_saveFragment_to_listFragment)
    }


}