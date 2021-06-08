package com.fasila.aqsalandmarks.ui

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.fasila.aqsalandmarks.R
import com.fasila.aqsalandmarks.app.AqsaLandmarksApplication
import com.fasila.aqsalandmarks.sendNotification

class MyBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val hearts = AqsaLandmarksApplication.sharedPref.getString("hearts", "5")
        if (hearts != "5") {
            AqsaLandmarksApplication.sharedPref.edit().putString("hearts", "5").apply()
            //Toast.makeText(context, "you recover all your hearts!", Toast.LENGTH_LONG).show()
            val notificationManager = ContextCompat.getSystemService(
                context!!,
                NotificationManager::class.java
            ) as NotificationManager
            notificationManager.sendNotification("يمكنك الآن مواصلة التعلم (:", context)
        }
    }
}