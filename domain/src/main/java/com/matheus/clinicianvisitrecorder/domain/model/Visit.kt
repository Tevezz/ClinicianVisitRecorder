package com.matheus.clinicianvisitrecorder.domain.model

data class Visit(
    val id: Int = 0,
    val patientId: String,
    val filePath: String,
    val timestamp: Long,
    val duration: String
)