package com.fasila.aqsalandmarks.ui.stages

import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fasila.aqsalandmarks.R
import com.fasila.aqsalandmarks.app.AqsaLandmarksApplication
import com.fasila.aqsalandmarks.model.badge.Badge
import com.fasila.aqsalandmarks.model.stage.Stage
import kotlinx.coroutines.launch
import timber.log.Timber

class StagesViewModel : ViewModel() {

    private val repository by lazy { AqsaLandmarksApplication.repository }

    var stages: LiveData<List<Stage>>

    var badges: LiveData<List<Badge>>

    init {
        stages = repository.getAllStages()
        badges = repository.getAllBadges()
    }

    private val _navigateToStageDialog = MutableLiveData<Stage>()
    val navigateToStageDialog
        get() = _navigateToStageDialog

    fun onStageClicked(stage: Stage) {
        _navigateToStageDialog.value = stage
    }

    fun onStageDialogNavigated() {
        _navigateToStageDialog.value = null
    }

    fun updateStages(stage: Stage) {
        viewModelScope.launch { repository.updateStage(stage)}
    }

    fun updateBadges(badge: Badge) {
        viewModelScope.launch { repository.updateBadge(badge) }
    }

    fun deleteBadgesUserData() {
        badges.value?.let {
            for (badge in it) {
                badge.achieve = 0
                updateBadges(badge)
            }
        }
    }

}