package com.fasila.aqsalandmarks.ui.card

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fasila.aqsalandmarks.app.AqsaLandmarksApplication
import com.fasila.aqsalandmarks.model.AqsaLmRepository
import com.fasila.aqsalandmarks.model.AqsaLmarksDb
import com.fasila.aqsalandmarks.model.card.Card
import com.fasila.aqsalandmarks.model.stage.Stage

class CardViewModel(application: Application, private val cardId: String) : AndroidViewModel(application) {

    private val repository by lazy { AqsaLandmarksApplication.repository }

    var card = MutableLiveData<Card>()

    var stage = MutableLiveData<Stage>()

    init {
        getCard(cardId)
        getStage(cardId)
    }

    private fun getCard(cardId: String) {
        //viewModelScope.launch {
        card.value = repository.getCardById(cardId)
        // }
    }

    private fun getStage(stageId: String) {
        //viewModelScope.launch {
        stage.value = repository.getStageById(stageId)
        // }
    }
}