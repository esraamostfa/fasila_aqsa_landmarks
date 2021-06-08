package com.fasila.aqsalandmarks.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fasila.aqsalandmarks.R
import com.fasila.aqsalandmarks.ui.profile.DeleteAccountDialogFragment
import com.fasila.aqsalandmarks.ui.stages.StagesFragment

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }
}