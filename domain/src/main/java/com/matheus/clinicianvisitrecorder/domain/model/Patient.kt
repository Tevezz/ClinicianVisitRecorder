package com.matheus.clinicianvisitrecorder.domain.model

data class Patient(
    val id: String,
    val name: String,
    val condition: String,
    val profileImageUrl: String,
    val species: String = "",
    val location: String = ""
)
