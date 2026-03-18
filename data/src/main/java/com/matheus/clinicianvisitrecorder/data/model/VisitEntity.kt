package com.matheus.clinicianvisitrecorder.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.matheus.clinicianvisitrecorder.domain.model.Visit

@Entity(tableName = "visits")
internal data class VisitEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val patientId: String,
    val filePath: String,
    val timestamp: Long,
    val duration: String
)

internal fun VisitEntity.toDomain() = Visit(
    id = id,
    patientId = patientId,
    filePath = filePath,
    timestamp = timestamp,
    duration = duration
)