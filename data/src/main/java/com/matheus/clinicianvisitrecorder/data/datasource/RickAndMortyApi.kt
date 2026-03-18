package com.matheus.clinicianvisitrecorder.data.datasource

import com.matheus.clinicianvisitrecorder.data.model.CharacterDto
import com.matheus.clinicianvisitrecorder.data.model.CharacterResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

internal class RickAndMortyApi @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getCharacters(page: Int): CharacterResponseDto =
        client.get("character") {
            parameter("page", page)
        }.body()

    suspend fun getCharacterById(id: String): CharacterDto =
        client.get("character/$id").body()
}