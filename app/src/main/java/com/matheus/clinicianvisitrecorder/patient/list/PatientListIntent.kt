package com.matheus.clinicianvisitrecorder.patient.list

sealed interface PatientListIntent {
    object Refresh : PatientListIntent
    data class OnPatientClick(val patientId: String) : PatientListIntent
}