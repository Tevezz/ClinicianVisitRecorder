package com.matheus.clinicianvisitrecorder.domain.repository

import com.matheus.clinicianvisitrecorder.domain.model.Visit
import kotlinx.coroutines.flow.Flow

interface VisitRepository {
    suspend fun saveVisit(visit: Visit)
    fun getVisitsForPatient(patientId: String): Flow<List<Visit>>
}