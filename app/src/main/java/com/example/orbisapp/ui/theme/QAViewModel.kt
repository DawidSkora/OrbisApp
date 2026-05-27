package com.example.orbisapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orbisapp.model.QAPair
import com.example.orbisapp.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QAViewModel : ViewModel() {

    private val _pairs = MutableStateFlow<List<QAPair>>(emptyList())
    val pairs: StateFlow<List<QAPair>> = _pairs

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadAll()
    }

    fun loadAll() {
        viewModelScope.launch {
            try {
                _pairs.value = RetrofitClient.api.getAll()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}