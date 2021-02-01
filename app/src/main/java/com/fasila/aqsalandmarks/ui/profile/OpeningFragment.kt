package com.fasila.aqsalandmarks.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.fasila.aqsalandmarks.R
import com.fasila.aqsalandmarks.databinding.FragmentOpeningBinding
import com.fasila.aqsalandmarks.databinding.FragmentRegisterBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import timber.log.Timber

class OpeningFragment : Fragment() {

    private lateinit var binding: FragmentOpeningBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_opening, container, false)

        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_openingFragment_to_loginFragment)
        }
        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_openingFragment_to_registerFragment)
        }
        binding.tryButton.setOnClickListener {
            findNavController().navigate(R.id.action_openingFragment_to_stagesFragment)
        }
        googleLogin()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        auth.currentUser?.let {
            if (it.isEmailVerified){
            findNavController().navigate(R.id.action_openingFragment_to_stagesFragment)
        } else {
            findNavController().navigate(R.id.action_openingFragment_to_loginFragment)
            }
        }
    }

    private fun googleLogin(){
        binding.googleSignButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RegisterFragment.RC_SIGN_IN)
        }

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RegisterFragment.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Timber.d("firebaseAuthWithGoogle:%s", account.id)
                firebaseAuthWithGoogle(account.idToken!!, auth, requireActivity()){
                    findNavController().navigate(R.id.action_openingFragment_to_stagesFragment)
                }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Timber.w(e, "Google sign in failed")
                Toast.makeText(activity, "فشل تسجيل الدخول باستخدام جوجل", Toast.LENGTH_SHORT).show()
                // [START_EXCLUDE]
                //updateUI(null)
                // [END_EXCLUDE]
            }
        }
    }

    private fun firebaseAuthWithGoogle(
        idToken: String,
        auth: FirebaseAuth,
        activity: Activity,
        updateUi: () -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("signInWithCredential:success")
                    val user = auth.currentUser
                    updateUi()
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.w(task.exception, "signInWithCredential:failure")
                }
            }
    }
}