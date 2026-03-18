package com.matheus.clinicianvisitrecorder.data.model

import com.matheus.clinicianvisitrecorder.domain.model.Patient

internal data class CharacterResponseDto(
    val info: InfoDto,
    val results: List<CharacterDto>
)

internal data class InfoDto(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)

internal data class LocationDto(
    val name: String,
    val url: String
)

internal data class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val location: LocationDto,
    val image: String
)

internal fun CharacterDto.toPatient() = Patient(
    id = id.toString(),
    name = name,
    condition = status,
    profileImageUrl = image,
    species = species,
    location = location.name
)