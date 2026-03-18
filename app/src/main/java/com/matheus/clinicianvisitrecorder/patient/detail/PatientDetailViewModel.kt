package com.matheus.clinicianvisitrecorder.patient.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matheus.clinicianvisitrecorder.domain.usecase.GetPatientByIdUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = PatientDetailViewModel.Factory::class)
class PatientDetailViewModel @AssistedInject constructor(
    @Assisted private val patientId: String,
    private val getPatientByIdUseCase: GetPatientByIdUseCase
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(patientId: String): PatientDetailViewModel
    }

    private val _uiState = MutableStateFlow(PatientDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadPatient()
    }

    fun handleIntent(intent: PatientDetailIntent) {
        when (intent) {
            is PatientDetailIntent.LoadPatient -> loadPatient()
            is PatientDetailIntent.StartVisit -> startRecording()
            is PatientDetailIntent.StopVisit -> stopRecording()
        }
    }

    private fun loadPatient() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // Fetching from our Clean Architecture domain layer
            getPatientByIdUseCase(patientId).collect { patient ->
                _uiState.update {
                    it.copy(patient = patient, isLoading = false)
                }
            }
        }
    }

    private fun startRecording() {
        // 1. Logic for Foreground Service will be triggered here
        // 2. Transition UI to Active state (Screenshot style)
        _uiState.update {
            it.copy(
                visitStatus = VisitStatus.Active,
                recordingDuration = "00:00",
                transcript = "Waiting for audio..."
            )
        }
    }

    private fun stopRecording() {
        _uiState.update { it.copy(visitStatus = VisitStatus.Idle) }
        // 3. Logic to kill the Foreground Service and save the file
    }
}