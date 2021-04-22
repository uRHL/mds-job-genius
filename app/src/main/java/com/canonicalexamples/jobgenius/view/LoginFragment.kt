package com.canonicalexamples.jobgenius.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.canonicalexamples.jobgenius.R
import com.canonicalexamples.jobgenius.app.JobGeniusApp
import com.canonicalexamples.jobgenius.databinding.FragmentLoginBinding
import com.canonicalexamples.jobgenius.model.user.User
import com.canonicalexamples.jobgenius.util.LoadImageURL
import com.canonicalexamples.jobgenius.util.SecureStorage
import com.canonicalexamples.jobgenius.viewmodels.LoginViewModel
import com.canonicalexamples.jobgenius.viewmodels.LoginViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.runBlocking


class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModels {
        val app = activity?.application as JobGeniusApp
        LoginViewModelFactory(app.database)
    }

    companion object{
        private const val RC_SIGN_IN = 120
    }

    private lateinit var bindingLoginFragment: FragmentLoginBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val secureStorage: SecureStorage = SecureStorage()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        bindingLoginFragment = FragmentLoginBinding.inflate(inflater, container, false)

        val view = bindingLoginFragment.root

        val app = activity?.application as JobGeniusApp
        auth = app.auth
        updateUIUserDetails(auth.currentUser)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        bindingLoginFragment.signInBtn.setOnClickListener{ signIn() }

        bindingLoginFragment.logOutBtn.setOnClickListener {
            signOut()
            Toast.makeText(view.context, "User logged out", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        runBlocking { auth.signOut() }
        println(auth.currentUser)
        updateUIUserDetails(auth.currentUser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
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
                        Toast.makeText(requireContext(), "User logged in", Toast.LENGTH_LONG).show()
                        updateUIUserDetails(auth.currentUser)
//                        val user = auth.currentUser
//                        if(user != null){
//
//                            upda
//
//
//                            insertDataToDatabase(user.displayName, user.email, user.photoUrl.toString())
//
//                            loginViewModel.userList.observe(viewLifecycleOwner) { userList ->
//
//                                val u = userList[userList.size - 1]
//
//                                val name = secureStorage.decryptData(Base64.decode(u.nameIV, Base64.DEFAULT), Base64.decode(u.nameEncodedText, Base64.DEFAULT))
//                                val mail = secureStorage.decryptData(Base64.decode(u.mailIV, Base64.DEFAULT), Base64.decode(u.mailEncodedText, Base64.DEFAULT))
//                                val image = secureStorage.decryptData(Base64.decode(u.imageIV, Base64.DEFAULT), Base64.decode(u.imageEncodedText, Base64.DEFAULT))
//
//                                bindingLoginFragment.userDetails.userNameText.text = "Welcome $name!"
//                                bindingLoginFragment.userDetails.userEmailText.text = mail
//
//                                val bitmap: Bitmap? = LoadImageURL().execute(image).get()
//                                bindingLoginFragment.userDetails.userAvatar.setImageBitmap(bitmap)
//                            }
//                        }
                    } else {
                        Log.w("Fragment Login", "signInWithCredential:failure", task.exception)
                    }
                }
    }


    private fun updateUIUserDetails(currentUser: FirebaseUser?){
        if(currentUser != null){
            bindingLoginFragment.userDetails.userNameText.text = "Welcome ${currentUser.displayName}!"
            bindingLoginFragment.userDetails.userEmailText.text = currentUser.email
            val bitmap: Bitmap? = LoadImageURL().execute(currentUser.photoUrl.toString()).get()
            bindingLoginFragment.userDetails.userAvatar.setImageBitmap(bitmap)
            bindingLoginFragment.logOutBtn.visibility = View.VISIBLE
        }else{
            // If there is no user logged in, log out button is not displayed, and UI is set to default
            bindingLoginFragment.userDetails.userNameText.text = resources.getString(R.string.userWelcomeText)
            bindingLoginFragment.userDetails.userEmailText.text = resources.getString(R.string.userEmailText)
            bindingLoginFragment.userDetails.userAvatar.setImageDrawable(resources.getDrawable( R.drawable.ic_user, null))
            bindingLoginFragment.logOutBtn.visibility = View.GONE
        }
    }

    private fun insertDataToDatabase(name: String, mail: String, image: String) {
        val namePair = secureStorage.encryptData(name)
        val mailPair = secureStorage.encryptData(mail)
        val imagePair = secureStorage.encryptData(image)

        val nameIV = Base64.encodeToString(namePair.first, Base64.DEFAULT)
        val nameEncodedText = Base64.encodeToString(namePair.second, Base64.DEFAULT)
        val mailIV = Base64.encodeToString(mailPair.first, Base64.DEFAULT)
        val mailEncodedText = Base64.encodeToString(mailPair.second, Base64.DEFAULT)
        val imageIV = Base64.encodeToString(imagePair.first, Base64.DEFAULT)
        val imageEncodedText = Base64.encodeToString(imagePair.second, Base64.DEFAULT)

        val user = User(
                0,
                nameIV,
                nameEncodedText,
                mailIV,
                mailEncodedText,
                imageIV,
                imageEncodedText
        )
        loginViewModel.addUser(user)
        Toast.makeText(requireContext(), "User logged in", Toast.LENGTH_LONG).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {

            R.id.action_favorites -> {
                findNavController().navigate(R.id.action_LoginFragment_to_JobFavListingFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}