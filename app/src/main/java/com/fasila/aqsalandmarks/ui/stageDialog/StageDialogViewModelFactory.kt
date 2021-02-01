package com.fasila.aqsalandmarks.ui.stageDialog

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class StageDialogViewModelFactory (private val application: Application, private val stageId: String) : ViewModelProvider.AndroidViewModelFactory(application) {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StageDialogViewModel::class.java)) {
            return StageDialogViewModel(application, stageId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}