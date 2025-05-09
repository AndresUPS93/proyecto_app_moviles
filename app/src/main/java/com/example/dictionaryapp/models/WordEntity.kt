package com.example.dictionaryapp.models
import androidx.room.PrimaryKey
import androidx.room.Entity


@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey val word: String,
    val phonetic: String?,
    val phoneticsJson: String,
    val meaningsJson: String
)
