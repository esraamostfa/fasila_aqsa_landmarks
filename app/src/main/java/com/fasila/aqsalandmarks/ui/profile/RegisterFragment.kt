package com.fasila.aqsalandmarks.ui.profile

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fasila.aqsalandmarks.R
import com.fasila.aqsalandmarks.databinding.FragmentRegisterBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.GoogleAuthProvider

class RegisterFragment : Fragment() {

    lateinit var viewModel: RegisterViewModel
    lateinit var binding: FragmentRegisterBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //initialize viewModel
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)

        binding.registerButton.setOnClickListener {
            val name = binding.usernameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            register(name,email, password)
            animateLogin()
        }

        return binding.root
    }

    private fun animateLogin() {
        binding.progressBar.alpha = 0f
        binding.progressBar.visibility = View.VISIBLE

        val alphaAnimator = ValueAnimator.ofFloat(0f, 1f)
        alphaAnimator.duration = 1000

        alphaAnimator.addUpdateListener {
            val animationAlpha = it.animatedValue as Float
            binding.progressBar.alpha = animationAlpha
            binding.container.alpha = 1 - animationAlpha
        }

        alphaAnimator.start()
    }


    override fun onStart() {
        super.onStart()
        auth.currentUser?.let{
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun register(name: String, email: String, password: String) {
        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailTextInput.error = "Please enter valid email"
            binding.emailTextInput.requestFocus()
        }
        if (password.isBlank() || password.length < 6) {
            binding.passwordTextInput.error = "6 characters password is required"
            binding.passwordTextInput.requestFocus()
        } else {
            viewModel.createUser(auth, email,password, name, requireActivity()) {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }
    }

//    private fun login(email: String, password: String) {
//        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            binding.emailTextInput.error = "Please enter valid email"
//            binding.emailTextInput.requestFocus()
//        }
//        if (password.isBlank() || password.length < 6) {
//            binding.passwordTextInput.error = "6 characters password is required"
//            binding.passwordTextInput.requestFocus()
//        } else {
//            auth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(requireActivity()) { task ->
//                    if (task.isSuccessful) {
//                        // Sign in success, update UI with the signed-in user's information
//                        Timber.d("signInWithEmail:success")
//                        findNavController().navigate(R.id.action_loginFragment_to_stagesFragment)
//                    } else {
//                        // If sign in fails, display a message to the user.
//                        Timber.w(task.exception, "signInWithEmail:failure")
//                        Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_LONG).show()
//                    }
//                }
//        }
//    }

                companion object {
                const val RC_SIGN_IN = 9001
            }
}