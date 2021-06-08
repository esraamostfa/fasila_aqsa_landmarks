package com.fasila.aqsalandmarks.ui.profile

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.fasila.aqsalandmarks.R
import com.fasila.aqsalandmarks.rootRef
import com.fasila.aqsalandmarks.ui.MainActivity
import com.fasila.aqsalandmarks.ui.stages.StagesViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber


class DeleteAccountDialogFragment : DialogFragment() {

    val user = Firebase.auth.currentUser
    private val auth = FirebaseAuth.getInstance()

    //private val viewModel: StagesViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it, R.style.ThemeOverlay_App_SettingsDialog)
            val view: View =
                requireActivity().layoutInflater.inflate(R.layout.delete_account_dialog, null)

            builder.setView(view)

            // Add action buttons
            builder.setPositiveButton("تأكيد حذف الحساب") { dialog, _ ->
                val email = getDialog()?.findViewById<EditText>(R.id.email_edit_text)
                //binding.emailEditText.text.toString().trim()
                val password = getDialog()?.findViewById<EditText>(R.id.password_edit_text)
                //binding.passwordEditText.text.toString().trim()

                try {
                    deleteUser(
                        requireContext(),
                        email?.text.toString().trim(),
                        password?.text.toString().trim()
                    )
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
                .setNegativeButton("الغاء") { dialog, _ ->
                    dialog.cancel()
                    dialog.dismiss()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun deleteUser(context: Context, email: String, password: String) {
        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(
                context,
                "من فضلك أدخل بريد إلكتروني صحيح",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (password.isBlank() || password.length < 6) {
            Toast.makeText(
                context,
                "من فضلك أدخل كلمة سر صحيحة",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val credential = EmailAuthProvider
                .getCredential(email, password)
            user!!.reauthenticate(credential)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        rootRef.child(user.uid).removeValue()
                        user.delete()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    rootRef.child(user.uid).removeValue()
                                    Timber.d("User account deleted.")
                                    Toast.makeText(
                                        context,
                                        "تم حذف الحساب بنجاح",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    (activity as MainActivity?)?.finish()
                                    context.startActivity(
                                        Intent(
                                            context,
                                            MainActivity::class.java
                                        )
                                    )
                                } else {
                                    Toast.makeText(
                                        context,
                                        "حدثت مشكلة أثناء حذف الحساب",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(
                            context,
                            "حدثت مشكلة أثناء حذف الحساب، تأكد من إدخال بيانتك بشكل صحيح",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}
