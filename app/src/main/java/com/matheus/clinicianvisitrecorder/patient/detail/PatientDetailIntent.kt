package com.matheus.clinicianvisitrecorder.patient.detail

sealed interface PatientDetailIntent {
    data class LoadPatient(val patientId: String) : PatientDetailIntent
    object StartVisit : PatientDetailIntent
    object StopVisit : PatientDetailIntent
}