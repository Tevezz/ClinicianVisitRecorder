package com.matheus.clinicianvisitrecorder.data.repository

import com.matheus.clinicianvisitrecorder.data.datasource.VisitDao
import com.matheus.clinicianvisitrecorder.data.model.VisitEntity
import com.matheus.clinicianvisitrecorder.data.model.toDomain
import com.matheus.clinicianvisitrecorder.domain.model.Visit
import com.matheus.clinicianvisitrecorder.domain.repository.VisitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class VisitRepositoryImpl @Inject constructor(
    private val visitDao: VisitDao
) : VisitRepository {

    override suspend fun saveVisit(visit: Visit) {
        val entity = VisitEntity(
            patientId = visit.patientId,
            filePath = visit.filePath,
            timestamp = visit.timestamp,
            duration = visit.duration
        )
        visitDao.insertVisit(entity)
    }

    override fun getVisitsForPatient(patientId: String): Flow<List<Visit>> {
        return visitDao.getVisitsForPatient(patientId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}