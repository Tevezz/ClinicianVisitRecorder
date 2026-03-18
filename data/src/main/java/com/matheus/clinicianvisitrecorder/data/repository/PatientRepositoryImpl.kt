package com.matheus.clinicianvisitrecorder.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.matheus.clinicianvisitrecorder.data.datasource.PatientDataSource
import com.matheus.clinicianvisitrecorder.domain.model.Patient
import com.matheus.clinicianvisitrecorder.domain.repository.PatientRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class PatientRepositoryImpl @Inject constructor() : PatientRepository {

    override fun getPatients(): Flow<PagingData<Patient>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { PatientDataSource() }
        ).flow
    }
}