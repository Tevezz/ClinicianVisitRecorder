package com.matheus.clinicianvisitrecorder.patient.detail

import com.matheus.clinicianvisitrecorder.domain.model.Patient

data class PatientDetailUiState(
    val patient: Patient? = null,
    val isLoading: Boolean = false,
    val visitStatus: VisitStatus = VisitStatus.Idle,
    val recordingDuration: String = "00:00",
    val transcript: String = ""
)
