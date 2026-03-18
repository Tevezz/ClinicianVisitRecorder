package com.matheus.clinicianvisitrecorder.domain.usecase

import com.matheus.clinicianvisitrecorder.domain.model.Patient
import com.matheus.clinicianvisitrecorder.domain.repository.PatientRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPatientByIdUseCase @Inject constructor(
    private val repository: PatientRepository
) {
    operator fun invoke(id: String): Flow<Patient?> = repository.getPatientById(id)
}