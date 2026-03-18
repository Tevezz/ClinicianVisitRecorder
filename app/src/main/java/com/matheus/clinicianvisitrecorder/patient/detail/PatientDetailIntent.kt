package com.matheus.clinicianvisitrecorder.patient.detail

sealed interface PatientDetailIntent {
    object StartVisit : PatientDetailIntent
    object StopVisit : PatientDetailIntent
    data class PlayRecording(val filePath: String) : PatientDetailIntent
}