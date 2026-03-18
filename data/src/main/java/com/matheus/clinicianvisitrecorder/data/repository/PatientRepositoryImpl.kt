package com.matheus.clinicianvisitrecorder.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.matheus.clinicianvisitrecorder.data.datasource.PatientDataSource
import com.matheus.clinicianvisitrecorder.domain.model.Patient
import com.matheus.clinicianvisitrecorder.domain.repository.PatientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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

    override fun getPatientById(id: String): Flow<Patient?> = flow {
        delay(300)

        val fakePatient = Patient(
            id = id,
            name = "Patient $id",
            condition = "Chronic Condition $id",
            profileImageUrl = ""
        )

        emit(fakePatient)
    }.flowOn(Dispatchers.IO)
}