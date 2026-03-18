package com.matheus.clinicianvisitrecorder.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.matheus.clinicianvisitrecorder.data.datasource.PatientDataSource
import com.matheus.clinicianvisitrecorder.data.model.toPatient
import com.matheus.clinicianvisitrecorder.data.remote.RickAndMortyApi
import com.matheus.clinicianvisitrecorder.domain.model.Patient
import com.matheus.clinicianvisitrecorder.domain.repository.PatientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class PatientRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi
) : PatientRepository {

    override fun getPatients(): Flow<PagingData<Patient>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { PatientDataSource(api) }
    ).flow

    override fun getPatientById(id: String): Flow<Patient?> = flow {
        emit(api.getCharacterById(id).toPatient())
    }.flowOn(Dispatchers.IO)
}
