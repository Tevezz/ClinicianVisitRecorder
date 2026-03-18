package com.matheus.clinicianvisitrecorder.domain.usecase

import com.matheus.clinicianvisitrecorder.domain.model.Visit
import com.matheus.clinicianvisitrecorder.domain.repository.VisitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVisitsUseCase @Inject constructor(
    private val repository: VisitRepository
) {
    operator fun invoke(patientId: String): Flow<List<Visit>> {
        return repository.getVisitsForPatient(patientId)
    }
}