package com.fasila.aqsalandmarks.ui.stageDialog

import android.app.Application
import androidx.lifecycle.*
import com.fasila.aqsalandmarks.app.AqsaLandmarksApplication
import com.fasila.aqsalandmarks.model.AqsaLmRepository
import com.fasila.aqsalandmarks.model.AqsaLmarksDb
import com.fasila.aqsalandmarks.model.stage.Stage

class StageDialogViewModel(application: Application, private val stageId: String) : AndroidViewModel(application) {

    private val repository by lazy { AqsaLandmarksApplication.repository }

    var stage = MutableLiveData<Stage>()

    init {
        getStage(stageId)
    }


    private fun getStage(stageId: String) {
        //viewModelScope.launch {
            stage.value = repository.getStageById(stageId)
       // }
    }



}