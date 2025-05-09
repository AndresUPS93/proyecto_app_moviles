package com.example.dictionaryapp.database

import com.example.dictionaryapp.models.WordEntity
import javax.inject.Inject

class WordRepository @Inject constructor(
    private val wordDao: WordDao
) {
    suspend fun getWord(word: String): WordEntity? = wordDao.getWord(word)

    suspend fun saveWord(wordEntity: WordEntity) = wordDao.insertWord(wordEntity)

    suspend fun getAllWords(): List<WordEntity> = wordDao.getAllWords()

    suspend fun deleteWord(word: WordEntity) {
        wordDao.deleteWord(word)
    }

}
