package com.fasila.aqsalandmarks.ui.card

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CardViewModelFactory (private val application: Application, private val cardId: String) : ViewModelProvider.AndroidViewModelFactory(application) {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardViewModel::class.java)) {
            return CardViewModel(application, cardId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}