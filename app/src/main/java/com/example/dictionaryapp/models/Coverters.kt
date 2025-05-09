package com.example.dictionaryapp.models

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json


class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromPhonetics(value: List<Phonetic>): String = json.encodeToString(value)

    @TypeConverter
    fun toPhonetics(value: String): List<Phonetic> = json.decodeFromString(value)

    @TypeConverter
    fun fromMeanings(value: List<Meaning>): String = json.encodeToString(value)

    @TypeConverter
    fun toMeanings(value: String): List<Meaning> = json.decodeFromString(value)
}
