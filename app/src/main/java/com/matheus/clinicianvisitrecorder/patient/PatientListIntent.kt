package com.matheus.clinicianvisitrecorder.patient

sealed interface PatientListIntent {
    object Refresh : PatientListIntent
    data class OnPatientClick(val patientId: String) : PatientListIntent
}