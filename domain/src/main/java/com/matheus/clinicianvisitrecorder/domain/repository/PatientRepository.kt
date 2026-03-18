package com.matheus.clinicianvisitrecorder.domain.repository

import androidx.paging.PagingData
import com.matheus.clinicianvisitrecorder.domain.model.Patient
import kotlinx.coroutines.flow.Flow

interface PatientRepository {
    fun getPatients(): Flow<PagingData<Patient>>
}