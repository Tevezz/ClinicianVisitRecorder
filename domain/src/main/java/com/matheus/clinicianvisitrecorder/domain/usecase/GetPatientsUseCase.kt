package com.matheus.clinicianvisitrecorder.domain.usecase

import androidx.paging.PagingData
import com.matheus.clinicianvisitrecorder.domain.model.Patient
import com.matheus.clinicianvisitrecorder.domain.repository.PatientRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPatientsUseCase @Inject constructor(
    private val repository: PatientRepository
) {
    operator fun invoke(): Flow<PagingData<Patient>> = repository.getPatients()
}