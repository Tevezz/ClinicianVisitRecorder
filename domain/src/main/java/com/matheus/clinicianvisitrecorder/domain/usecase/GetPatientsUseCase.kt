package com.matheus.clinicianvisitrecorder.domain.usecase

import com.matheus.clinicianvisitrecorder.domain.repository.PatientRepository
import javax.inject.Inject

class GetPatientsUseCase @Inject constructor(
    private val patientRepository: PatientRepository
) {
}