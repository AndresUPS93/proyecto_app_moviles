package com.example.dictionaryapp.utils

import com.example.dictionaryapp.models.WordResponse
import com.example.dictionaryapp.models.WordEntity
import kotlinx.serialization.json.Json

fun WordResponse.toEntity(): WordEntity {
    val json = Json { prettyPrint = true }
    return WordEntity(
        word = word ?: "unknown",
        phonetic = phonetic,
        phoneticsJson = json.encodeToString(phonetics),
        meaningsJson = json.encodeToString(meanings)
    )
}

fun WordEntity.toResponse(): WordResponse {
    val json = Json { ignoreUnknownKeys = true }
    return WordResponse(
        word = word,
        phonetic = phonetic,
        phonetics = json.decodeFromString(phoneticsJson),
        meanings = json.decodeFromString(meaningsJson)
    )
}
