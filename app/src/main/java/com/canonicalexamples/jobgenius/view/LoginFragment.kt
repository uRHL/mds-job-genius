package com.canonicalexamples.jobgenius.view

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.canonicalexamples.jobgenius.R
import com.canonicalexamples.jobgenius.app.JobGeniusApp
import com.canonicalexamples.jobgenius.databinding.FragmentLoginBinding
import com.canonicalexamples.jobgenius.util.LoadImageURL
import com.canonicalexamples.jobgenius.util.SecureStorage
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.runBlocking


class LoginFragment : Fragment() {

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
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

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
            bindingLoginFragment.signInBtn.visibility = View.GONE
        }else{
            // If there is no user logged in, log out button is not displayed, and UI is set to default
            bindingLoginFragment.userDetails.userNameText.text = resources.getString(R.string.userWelcomeText)
            bindingLoginFragment.userDetails.userEmailText.text = resources.getString(R.string.userEmailText)
            bindingLoginFragment.userDetails.userAvatar.setImageDrawable(resources.getDrawable( R.drawable.ic_user, null))
            bindingLoginFragment.logOutBtn.visibility = View.GONE
            bindingLoginFragment.signInBtn.visibility = View.VISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {

            R.id.action_favorites -> {
                val app = activity?.application as JobGeniusApp
                if(app.auth.currentUser != null) {
                    findNavController().navigate(R.id.action_LoginFragment_to_JobFavListingFragment)
                    true
                }else {
                    Toast.makeText(app.applicationContext, "You need to be logged in to see your saved jobs", Toast.LENGTH_LONG).show()
                    false
                }
            }

            R.id.action_search -> {
                findNavController().navigate(R.id.action_LoginFragment_to_SearchFragment)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}