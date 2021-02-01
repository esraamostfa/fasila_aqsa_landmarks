package com.fasila.aqsalandmarks.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.widget.Toast
import com.fasila.aqsalandmarks.R
import com.fasila.aqsalandmarks.app.AqsaLandmarksApplication

class MyBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        AqsaLandmarksApplication.sharedPref.edit().putString("hearts", "5").apply()
        Toast.makeText(context, "you recover all your hearts!", Toast.LENGTH_LONG).show();
    }
}