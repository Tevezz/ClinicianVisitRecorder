package com.matheus.clinicianvisitrecorder.patient.detail

import com.matheus.clinicianvisitrecorder.domain.model.Patient
import com.matheus.clinicianvisitrecorder.domain.model.Visit

data class PatientDetailUiState(
    val patient: Patient? = null,
    val isLoading: Boolean = false,
    val visitStatus: VisitStatus = VisitStatus.Idle,
    val recordingDuration: String = "00:00",
    val transcript: String = "",
    val visits: List<Visit> = emptyList()
)
