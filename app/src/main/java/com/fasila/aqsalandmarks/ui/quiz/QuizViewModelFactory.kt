package com.fasila.aqsalandmarks.ui.quiz

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class QuizViewModelFactory (private val application: Application, private val quizId: String) :
    ViewModelProvider.AndroidViewModelFactory (application) {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            return QuizViewModel(application, quizId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}