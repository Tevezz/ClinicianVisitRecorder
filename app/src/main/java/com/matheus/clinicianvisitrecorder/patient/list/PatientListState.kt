package com.matheus.clinicianvisitrecorder.patient.list

import androidx.paging.PagingData
import com.matheus.clinicianvisitrecorder.domain.model.Patient
import kotlinx.coroutines.flow.Flow

data class PatientListState(
    val pagingData: Flow<PagingData<Patient>>
)
