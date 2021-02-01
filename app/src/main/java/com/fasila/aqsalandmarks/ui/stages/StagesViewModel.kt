package com.fasila.aqsalandmarks.ui.stages

import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fasila.aqsalandmarks.R
import com.fasila.aqsalandmarks.app.AqsaLandmarksApplication
import com.fasila.aqsalandmarks.model.badge.Badge
import com.fasila.aqsalandmarks.model.stage.Stage

class StagesViewModel : ViewModel() {

    private val repository by lazy { AqsaLandmarksApplication.repository }

    val stages: LiveData<List<Stage>>

    val badges: LiveData<List<Badge>>

    init {
        stages = repository.getAllStages()
        badges = repository.getAllBadges()
    }

    private val _navigateToStageDialog = MutableLiveData<String>()
    val navigateToStageDialog
        get() = _navigateToStageDialog

    fun onStageClicked(stageId: String) {
        _navigateToStageDialog.value = stageId
    }

    fun onStageDialogNavigated() {
        _navigateToStageDialog.value = null
    }
}