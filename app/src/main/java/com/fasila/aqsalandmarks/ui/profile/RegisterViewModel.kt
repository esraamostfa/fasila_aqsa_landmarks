package com.fasila.aqsalandmarks.ui.profile

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fasila.aqsalandmarks.app.AqsaLandmarksApplication
import com.fasila.aqsalandmarks.model.profile.Profile
import com.fasila.aqsalandmarks.model.profile.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserInfo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.profile_back_drop.view.*
import kotlinx.coroutines.launch
import timber.log.Timber

class RegisterViewModel() : ViewModel() {

    private val repository by lazy { AqsaLandmarksApplication.repository }

    fun createUser(
        auth: FirebaseAuth,
        email: String,
        password: String,
        name: String,
        activity: Activity,
        updateUi: () -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("createUserWithEmail:success")

                    val user = FirebaseAuth.getInstance().currentUser
                    user?.let {
                        it.sendEmailVerification().addOnSuccessListener {
                        Toast.makeText(activity, "راجع بريدك الإلكتروني، تم إرسال رسالة تأكيد", Toast.LENGTH_SHORT).show()
                    }
                        .addOnFailureListener{
                            Timber.d("failed to send email verification")
                        }
                    }

                    val profileUpdates = userProfileChangeRequest {
                        displayName = name
                        //photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
                    }

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener {
                            if (it.isSuccessful) {
                                Timber.d("User profile updated.")
                            }
                        }
                    //user?.let {insertUser(it)}
                    updateUi()

                } else {
                    // If sign in fails, display a message to the user.
                    Timber.w(task.exception, "createUserWithEmail:failure")
                    Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun insertUser(user: FirebaseUser) {
        repository.insertProfile(Profile(user = User(user.uid, user.displayName!!, user.email!!)))
    }
}
