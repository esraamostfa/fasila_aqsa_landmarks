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
import com.fasila.aqsalandmarks.R
import com.fasila.aqsalandmarks.ui.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class ChangePasswordDialogFragment : DialogFragment() {

    val user = Firebase.auth.currentUser

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it, R.style.ThemeOverlay_App_SettingsDialog)
            val view: View =
                requireActivity().layoutInflater.inflate(R.layout.fragment_change_password_dialog, null)

            builder.setView(view)

            // Add action buttons
            builder.setPositiveButton("تأكيد") { dialog, _ ->
                val email = getDialog()?.findViewById<EditText>(R.id.email_edit_text)?.text.toString().trim()
                val oldPass = getDialog()?.findViewById<EditText>(R.id.oldPassword_edit_text)?.text.toString().trim()
                val newPass = getDialog()?.findViewById<EditText>(R.id.newPassword_edit_text)?.text.toString().trim()

                try {
                    changePassword(requireContext(), email, oldPass, newPass)

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

    private fun changePassword(context: Context, email: String, oldPass: String, newPass:String) {

        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(
                context,
                "من فضلك أدخل بريد إلكتروني صحيح",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (oldPass.isBlank() || oldPass.length < 6) {
            Toast.makeText(
                context,
                "من فضلك أدخل كلمة سر صحيحة",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (newPass.isBlank() || newPass.length < 6) {
            Toast.makeText(
                context,
                "فشل تغيير كلمة السر، لابد أن تتكون كلمة السر من 6 أرقام أو حروف على الأقل",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val credential = EmailAuthProvider
                .getCredential(email, oldPass)
            user!!.reauthenticate(credential)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                    user.updatePassword(newPass)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Timber.d("User password updated.")
                                Toast.makeText(
                                    context,
                                    "تم تغيير كلمة السر بنجاح",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "حدثت مشكلة أثناء تغيير كلمة السر، تأكد من إدخال بيانتك بشكل صحيح",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else{
                        Toast.makeText(
                            context,
                            "حدثت مشكلة أثناء تغيير كلمة السر، تأكد من إدخال بيانتك بشكل صحيح",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
        }
    }


}