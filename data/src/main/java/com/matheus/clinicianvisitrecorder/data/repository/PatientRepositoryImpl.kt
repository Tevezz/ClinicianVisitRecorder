package com.matheus.clinicianvisitrecorder.data.repository

import com.matheus.clinicianvisitrecorder.domain.model.Patient
import com.matheus.clinicianvisitrecorder.domain.repository.PatientRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

internal class PatientRepositoryImpl @Inject constructor() : PatientRepository {
    override fun getPatients(): Flow<List<Patient>> = flowOf(
        listOf(
            Patient("1", "Robert Chen", "Post-op Recovery", "", "1978-04-12", "Follow-up"),
            Patient("2", "Maria Santos", "Hypertension", "", "1965-09-23", "Routine Checkup"),
            Patient("3", "James Okafor", "Type 2 Diabetes", "", "1982-01-07", "Routine Checkup"),
            Patient("4", "Aisha Patel", "Asthma", "", "1990-11-30", "Emergency"),
            Patient("5", "Lucas Fernandez", "Acute Bronchitis", "", "2001-06-15", "First Visit"),
        )
    )
}