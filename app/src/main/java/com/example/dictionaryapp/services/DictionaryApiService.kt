package com.example.dictionaryapp.services


import com.example.dictionaryapp.models.WordResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

class DictionaryApiService @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getWords(word: String): List<WordResponse> { // Cambia el tipo de retorno a List<WordResponse>
        val response: HttpResponse = client.get(urlString="https://api.dictionaryapi.dev/api/v2/entries/en/$word")
        return response.body() // Ktor debería deserializar el array JSON a una lista
    }
}