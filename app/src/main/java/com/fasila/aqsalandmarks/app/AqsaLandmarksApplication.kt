package com.fasila.aqsalandmarks.app

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.SystemClock
import com.fasila.aqsalandmarks.BuildConfig
import com.fasila.aqsalandmarks.model.AqsaLmRepository
import com.fasila.aqsalandmarks.model.AqsaLmarksDb
import com.fasila.aqsalandmarks.ui.MyBroadcastReceiver
import kotlinx.coroutines.GlobalScope
import timber.log.Timber
import timber.log.Timber.DebugTree
import timber.log.Timber.i
import java.util.*


class AqsaLandmarksApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        myPackageName = applicationContext.packageName
        appInstance = this

        val interval: Long = 1 * 60 * 60 * 1000
        val alarmManager =
            appInstance.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val intent = Intent(this, MyBroadcastReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(this.applicationContext, 1313, intent, 0)
        alarmManager?.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + interval,
            interval, pendingIntent
        )

        val hearts = sharedPref.getString("hearts", null)
        if ( hearts == null) sharedPref.edit().putString("hearts", "5").apply()
    }

    companion object {
        private const val PREFERENCE_FILE_KEY = "small_data"
        val sharedPref: SharedPreferences by lazy {
            appInstance.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE)
        }
        lateinit var myPackageName: String
        private lateinit var appInstance: AqsaLandmarksApplication
        private val database by lazy { AqsaLmarksDb.getInstance(appInstance, GlobalScope) }
        val repository: AqsaLmRepository by lazy {
            AqsaLmRepository(
                database.stageDao,
                database.cardDao,
                database.quizDao,
                database.profileDao,
                database.badgeDao
            )
        }

        fun calculateStreak(openNewStage: Boolean) {
            val calendar = Calendar.getInstance()
            val thisDay: Int = calendar.get(Calendar.DAY_OF_YEAR) // GET THE CURRENT DAY OF THE YEAR
            val lastDay = sharedPref.getInt("lastDay", 0) //If we don't have a saved value, use 0.
            var streakCounter =
                sharedPref.getInt("streakCounter", 0) //If we don't have a saved value, use 0.

            if (openNewStage) {
                if (lastDay == thisDay - 1 && openNewStage) {
                    // CONSECUTIVE DAYS
                    streakCounter += 1
                    sharedPref.edit().putInt("lastDay", thisDay).apply()
                    sharedPref.edit().putInt("streakCounter", streakCounter).apply()
                    Timber.d(lastDay.toString())
                } else if (lastDay == thisDay) {
                    //do nothing
                } else {
                    sharedPref.edit().putInt("lastDay", thisDay).apply()
                    sharedPref.edit().putInt("streakCounter", 1).apply()
                    Timber.d(lastDay.toString())
                }
            }
            else{
                //last day NOT yesterday AND last day NOT today
                if (lastDay != thisDay-1 &&  lastDay != thisDay)
                {
                    sharedPref.edit().putInt("streakCounter", 0).apply()
                }
            }
        }

    }
}