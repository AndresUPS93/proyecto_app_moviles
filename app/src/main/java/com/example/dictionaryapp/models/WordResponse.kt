package com.example.dictionaryapp.models

import kotlinx.serialization.Serializable

@Serializable
data class Phonetic(
    val text: String? = null,
    val audio: String? = null
)

@Serializable
data class Definition(
    val definition: String? = null,
    val example: String? = null,

)

@Serializable
data class Meaning(
    val partOfSpeech: String? = null,
    val definitions: List<Definition> = emptyList(),
    val synonyms: List<String> = emptyList(),
    val antonyms: List<String> = emptyList()
)

@Serializable
data class WordResponse(
    val word: String? = null,
    val phonetic: String? = null,
    val phonetics: List<Phonetic> = emptyList(),
    val origin: String? = null,
    val meanings: List<Meaning> = emptyList()
)