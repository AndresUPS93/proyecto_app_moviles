package com.example.dictionaryapp.viewModels

import com.example.dictionaryapp.database.WordRepository
import com.example.dictionaryapp.models.WordEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@HiltViewModel
class OfflineWordsViewModel @Inject constructor(
    private val repository: WordRepository
) : ViewModel() {

    private val _offlineWords = MutableStateFlow<List<WordEntity>>(emptyList())
    val offlineWords: StateFlow<List<WordEntity>> = _offlineWords

    fun loadOfflineWords() {
        viewModelScope.launch {
            _offlineWords.value = repository.getAllWords()
        }
    }

    fun saveWord(word: WordEntity) {
        viewModelScope.launch {
            repository.saveWord(word)
            loadOfflineWords() // Actualiza la lista
        }
    }

    fun deleteWord(word: WordEntity) {
        viewModelScope.launch {
            repository.deleteWord(word)
            loadOfflineWords() // Recarga la lista después de eliminar
        }
    }

}
