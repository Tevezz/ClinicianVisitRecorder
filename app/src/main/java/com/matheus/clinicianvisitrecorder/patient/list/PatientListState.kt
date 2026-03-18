package com.matheus.clinicianvisitrecorder.patient.list

data class PatientListState(
    val title: String = "My Patients",
    val isRefreshing: Boolean = false,
    val error: String? = null
)
