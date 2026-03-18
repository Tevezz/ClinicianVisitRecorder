package com.matheus.clinicianvisitrecorder.patient.list

sealed interface PatientListIntent {
    data class SelectPatient(val patientId: String) : PatientListIntent
}
