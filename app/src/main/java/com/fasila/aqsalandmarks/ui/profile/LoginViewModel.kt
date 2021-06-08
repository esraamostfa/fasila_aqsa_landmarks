package com.fasila.aqsalandmarks.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fasila.aqsalandmarks.rootRef
import com.fasila.aqsalandmarks.app.AqsaLandmarksApplication
import com.fasila.aqsalandmarks.model.badge.Badge
import com.fasila.aqsalandmarks.model.realtime.Badge as RealBadge
import com.fasila.aqsalandmarks.model.stage.Stage
import com.fasila.aqsalandmarks.model.realtime.Stage as RealStage
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginViewModel() : ViewModel() {

    private val repository by lazy { AqsaLandmarksApplication.repository }
    var stage = MutableLiveData<Stage>()

    init {

    }

    private fun getStage(stageId: String): Stage {
        return repository.getStageById(stageId)
    }

    private fun getBadge(badgeId: String): Badge {
        return repository.getBadgeById(badgeId)
    }

    fun importStagesUserData(user: FirebaseUser) {
        val stages = mutableListOf<RealStage>()

        val stagesListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (stageSnapshot in dataSnapshot.children) {
                    if (stageSnapshot.getValue<RealStage>() != null) {
                        stages.add(stageSnapshot.getValue<RealStage>() as RealStage)

                        for (stage in stages) {
                            val roomStage = getStage(stage.stageId)
                            roomStage.score = stage.score
                            roomStage.passed = stage.passed

                            viewModelScope.launch { repository.updateStage(roomStage) }
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Timber.w(databaseError.toException())
            }
        }
        val ref = rootRef.child(user.uid).child("stages")
        ref.addListenerForSingleValueEvent(stagesListener)
        //ref.addValueEventListener(stagesListener)
    }

    fun importBadgesUserData(user: FirebaseUser) {
        val badges = mutableListOf<RealBadge>()

        val badgesListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (badgeSnapshot in dataSnapshot.children) {
                    if (badgeSnapshot.getValue<RealBadge>() != null) {
                        badges.add(badgeSnapshot.getValue<RealBadge>() as RealBadge)
                        //Timber.i(badgeSnapshot.getValue<RealBadge>().toString())

                        for (badge in badges) {
                            val roomBadge = getBadge(badge.id)
                            roomBadge.achieve = badge.achieve
                            viewModelScope.launch { repository.updateBadge(roomBadge) }
                            Timber.i(roomBadge.achieve.toString())
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Timber.w(databaseError.toException())
            }
        }
        val ref = rootRef.child(user.uid).child("badges")
        ref.addListenerForSingleValueEvent(badgesListener)
        //ref.addValueEventListener(stagesListener)
    }

    fun importUserStreak(user: FirebaseUser) {
        val streakListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val streak = snapshot.getValue<Int>()
                Timber.i(streak.toString())
                streak?.let {
                    AqsaLandmarksApplication.sharedPref.edit().putInt("streakCounter", streak)
                        .apply()
                    Timber.i(AqsaLandmarksApplication.sharedPref.getInt("streakCounter", 0).toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.w(error.toException())
            }
        }
        val streakRef = rootRef.child(user.uid).child("streak")
        streakRef.addListenerForSingleValueEvent(streakListener)
    }

    fun importUserLastDayStreak(user: FirebaseUser) {
        val lastDayListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lastDay = snapshot.getValue<Int>()
                Timber.i(lastDay.toString())
                lastDay?.let {
                    AqsaLandmarksApplication.sharedPref.edit().putInt("lastDay", lastDay)
                        .apply()
                    Timber.i(AqsaLandmarksApplication.sharedPref.getInt("lastDay", 0).toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.w(error.toException())
            }
        }
        val lastDayRef = rootRef.child(user.uid).child("lastDay")
        lastDayRef.addListenerForSingleValueEvent(lastDayListener)
    }


    fun importUserHearts(user: FirebaseUser) {
        val heartsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val hearts = snapshot.getValue<String>()
                Timber.i(hearts.toString())
                hearts?.let {
                    AqsaLandmarksApplication.sharedPref.edit().putString("hearts", hearts)
                        .apply()
                    Timber.i(AqsaLandmarksApplication.sharedPref.getString("hearts", "5").toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.w(error.toException())
            }
        }
        val heartsRef = rootRef.child(user.uid).child("hearts")
        heartsRef.addListenerForSingleValueEvent(heartsListener)
    }

}