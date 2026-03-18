package com.matheus.clinicianvisitrecorder.data.datasource

import com.matheus.clinicianvisitrecorder.data.model.CharacterDto
import com.matheus.clinicianvisitrecorder.data.model.CharacterResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface RickAndMortyApi {

    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int): CharacterResponseDto

    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id") id: String): CharacterDto
}