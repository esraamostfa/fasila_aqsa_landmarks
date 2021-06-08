package com.fasila.aqsalandmarks.ui.quiz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fasila.aqsalandmarks.app.AqsaLandmarksApplication
import com.fasila.aqsalandmarks.model.badge.Badge
import com.fasila.aqsalandmarks.model.card.Card
import com.fasila.aqsalandmarks.model.quiz.Quiz
import com.fasila.aqsalandmarks.model.stage.Stage
import com.fasila.aqsalandmarks.rootRef
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import java.util.*

class QuizViewModel(application: Application, private val cardId: String) :
    AndroidViewModel(application) {

    private val repository by lazy { AqsaLandmarksApplication.repository }

    //var card = MutableLiveData<Card>()
    var quizzes = MutableLiveData<List<Quiz>>()
    var nextStage = MutableLiveData<Stage>()
    var stage = MutableLiveData<Stage>()
    lateinit var badge: Badge

    init {
        //getCard(cardId)
        getQuizzes(cardId)

        val stagesBeforeHeaders = listOf(15, 21, 29, 46, 60, 64, 74, 90, 95)
        if(cardId.toInt() in stagesBeforeHeaders) {
            getNextStage((cardId.toInt() + 2).toString())
        }
        else {
            getNextStage((cardId.toInt() + 1).toString())
        }
        getStage(cardId)
        getBadge(stage.value!!)

    }

    fun getCard(cardId: String) : Card {
        //viewModelScope.launch {
        return repository.getCardById(cardId)
        // }
    }

    private fun getQuizzes(stageId: String) {
        val quizzesByStage = repository.getQuizzesByStage(stageId)
        quizzes.value = quizzesByStage.quizzes
    }

    private fun getNextStage(stageId: String) {
        nextStage.value = repository.getStageById(stageId)
    }

    private fun getStage(stageId: String) {
        stage.value = repository.getStageById(stageId)
    }

    fun updateStage(stage: Stage) {
        viewModelScope.launch { repository.updateStage(stage) }
    }

    private fun getBadge(stage: Stage) {
        val badgeId = stage.badgeId
        badge = repository.getBadgeById(badgeId)

    }

    fun updateBadgeAchievement(user: FirebaseUser?) {
        //ensure it's first time to open next stage
        if (!nextStage.value?.passed!!) {
            badge.achieve++
            val realtimeBadge =
                com.fasila.aqsalandmarks.model.realtime.Badge(badge.id, badge.achieve)
            viewModelScope.launch { repository.updateBadge(badge) }
            user?.let {
                rootRef.child(it.uid).child("badges").child(badge.id).setValue(realtimeBadge)
            }
        }
    }
}