package com.matheus.clinicianvisitrecorder.patient.list

sealed interface PatientListEvent {
    data class NavigateToDetail(val patientId: String) : PatientListEvent
}
