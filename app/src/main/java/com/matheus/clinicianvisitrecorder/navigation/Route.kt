package com.matheus.clinicianvisitrecorder.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object PatientList : Route

    @Serializable
    data class PatientDetail(val patientId: String) : Route
}