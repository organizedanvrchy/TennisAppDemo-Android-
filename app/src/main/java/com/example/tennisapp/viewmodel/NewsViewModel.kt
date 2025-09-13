package com.example.tennisapp.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tennisapp.data.TennisNewsRepository
import com.example.tennisapp.model.news.NewsArticle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsViewModel(
    private val repo: TennisNewsRepository = TennisNewsRepository()
) : ViewModel() {

    data class UiState(
        val loading: Boolean = false,
        val articles: List<NewsArticle> = emptyList(),
        val error: String? = null
    )

    private val _uiState = mutableStateOf(UiState())
    val uiState: State<UiState> = _uiState

    fun load() = refresh()

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)
            val result = withContext(Dispatchers.IO) {
                runCatching { repo.fetchAllRoundRobin() } // interleaved by source
            }
            _uiState.value = result.fold(
                onSuccess = { list -> _uiState.value.copy(loading = false, articles = list) },
                onFailure = { err -> _uiState.value.copy(loading = false, error = err.message ?: "Unknown error") }
            )
        }
    }
}
