package com.fasila.aqsalandmarks.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.fasila.aqsalandmarks.R

class SplashActivity : AppCompatActivity() {

    //private val animation: Animation
    private val splashTime: Long = 3000 // 3 sec

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //animation = AnimationUtils.loadAnimation(this, R.anim.slide_from_top)

        /*animation.setAnimationListener(Animation.AnimationListener) {
            override fun  on
        }*/

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, splashTime)

        supportActionBar?.hide()
    }
}