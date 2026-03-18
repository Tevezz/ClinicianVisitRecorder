package com.matheus.clinicianvisitrecorder.data.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.matheus.clinicianvisitrecorder.data.model.VisitEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface VisitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisit(visit: VisitEntity)

    @Query("SELECT * FROM visits WHERE patientId = :patientId ORDER BY timestamp DESC")
    fun getVisitsForPatient(patientId: String): Flow<List<VisitEntity>>
}