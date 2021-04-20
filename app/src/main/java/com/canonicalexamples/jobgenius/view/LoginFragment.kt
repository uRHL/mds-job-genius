package com.canonicalexamples.jobgenius.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.canonicalexamples.jobgenius.R
import com.canonicalexamples.jobgenius.app.JobGeniusApp
import com.canonicalexamples.jobgenius.databinding.FragmentLoginBinding
import com.canonicalexamples.jobgenius.model.User
import com.canonicalexamples.jobgenius.util.LoadImageURL
import com.canonicalexamples.jobgenius.viewmodels.LoginViewModel
import com.canonicalexamples.jobgenius.viewmodels.LoginViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.net.URL
import javax.crypto.KeyGenerator


class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModels {
        val app = activity?.application as JobGeniusApp
        LoginViewModelFactory(app.database)
    }

    companion object{
        private const val RC_SIGN_IN = 120
    }

    private lateinit var bindingLoginFragment: FragmentLoginBinding
    //private lateinit var bindingUserDetailsFragment: FragmentUserDetailsBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingLoginFragment = FragmentLoginBinding.inflate(inflater, container, false)
        //bindingUserDetailsFragment = FragmentUserDetailsBinding.inflate(inflater, container, false)

        val view = bindingLoginFragment.root

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        // Create out KeyStore instance, iff it is not created
        if(!loginViewModel.checkKey()){
            generateKey()
        }

        if(user != null){
            // We do not want to navigate to search if there is a user already logged. Instead we want to show account details
            //findNavController().navigate(R.id.action_LoginFragment_to_SearchFragment)

        }



        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        bindingLoginFragment.signInBtn.setOnClickListener{
            signIn()
        }

        return view
    }

    private fun generateKey() {
        // Needed to update minSdkVersion to 23
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            "AndroidKeyStore"
        )
        val keyGenParameterSpec = KeyGenParameterSpec
            .Builder("MyKeyStore", KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()

        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
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
//                Log.d("Fragment Login", "firebaseAuthWithGoogle:" + account.id)
//                Log.d("****** ", account.email + "\t" + account.displayName + "\t" + account.photoUrl)
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
                            Log.d(
                                "****** ",
                                user.email + "\t" + user.photoUrl + "\t" + user.displayName
                            )
                            insertDataToDatabase(
                                user.displayName,
                                user.email,
                                user.photoUrl.toString()
                            )
                            // Update the UI
                            bindingLoginFragment.userDetails.userNameText.text = "Welcome " + user.displayName + "!"
                            bindingLoginFragment.userDetails.userEmailText.text = user.email

                            val bitmap: Bitmap? = LoadImageURL().execute(user.photoUrl.toString()).get()
                            bindingLoginFragment.userDetails.userAvatar.setImageBitmap(bitmap)


                            //findNavController().navigate(R.id.action_LoginFragment_to_SearchFragment)
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

        val user = User(
            0,
            nameIV,
            nameEncodedText,
            mailIV,
            mailEncodedText,
            imageIV,
            imageEncodedText
        )
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