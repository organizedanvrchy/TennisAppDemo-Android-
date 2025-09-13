package com.example.tennisapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tennisapp.data.TennisRepository

class TennisViewModelFactory(
    private val repository: TennisRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TennisViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TennisViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
