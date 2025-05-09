package com.example.dictionaryapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionaryapp.models.WordResponse
import com.example.dictionaryapp.services.DictionaryApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordsListViewModel @Inject constructor(
    private val dictionaryApiService: DictionaryApiService
) : ViewModel() {

    private val _words = MutableStateFlow<List<WordResponse>>(emptyList())
    val words: StateFlow<List<WordResponse>> = _words

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun searchWord(word: String) {
        if (word.isBlank()) {
            _errorMessage.value = "Por favor ingresa una palabra válida."
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = dictionaryApiService.getWords(word)
                _words.value = result
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = getFriendlyErrorMessage(e)
                _words.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun getFriendlyErrorMessage(exception: Exception): String {
        return when {
            exception.message?.contains("Unable to resolve host", ignoreCase = true) == true ->
                "Parece que no tienes conexión a Internet."

            exception.message?.contains("timeout", ignoreCase = true) == true ->
                "La conexión está tardando demasiado. Intenta de nuevo."

            exception.message?.contains("404") == true ->
                "No se encontró la palabra ingresada."

            else -> "Ocurrió un error inesperado. Intenta más tarde."
        }
    }
}
