package com.matheus.clinicianvisitrecorder.patient.detail

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matheus.clinicianvisitrecorder.domain.usecase.GetPatientByIdUseCase
import com.matheus.clinicianvisitrecorder.domain.usecase.GetVisitsUseCase
import com.matheus.clinicianvisitrecorder.service.RecordingService
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = PatientDetailViewModel.Factory::class)
class PatientDetailViewModel @AssistedInject constructor(
    @Assisted private val patientId: String,
    @ApplicationContext private val context: Context,
    private val getPatientByIdUseCase: GetPatientByIdUseCase,
    private val getVisitsUseCase: GetVisitsUseCase
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(patientId: String): PatientDetailViewModel
    }

    private val _uiState = MutableStateFlow(PatientDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadPatient()
        loadVisits()
    }

    fun handleIntent(intent: PatientDetailIntent) {
        when (intent) {
            is PatientDetailIntent.StartVisit -> startRecording()
            is PatientDetailIntent.StopVisit -> stopRecording()
            is PatientDetailIntent.PlayRecording -> playRecording(intent.filePath)
        }
    }

    private fun loadPatient() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getPatientByIdUseCase(patientId).collect { patient ->
                _uiState.update {
                    it.copy(patient = patient, isLoading = false)
                }
            }
        }
    }

    private fun loadVisits() {
        viewModelScope.launch {
            getVisitsUseCase(patientId).collect { visitList ->
                _uiState.update { it.copy(visits = visitList) }
            }
        }
    }

    private fun playRecording(filePath: String) {
        val file = java.io.File(filePath)
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "audio/*")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    private fun startRecording() {
        val intent = Intent(context, RecordingService::class.java).apply {
            action = RecordingService.ACTION_START
            putExtra(RecordingService.EXTRA_PATIENT_ID, patientId)
        }

        ContextCompat.startForegroundService(context, intent)

        _uiState.update {
            it.copy(
                visitStatus = PatientDetailVisitStatus.Active,
                recordingDuration = "00:00",
                transcript = ""
            )
        }

        startTimer()
    }

    private fun stopRecording() {
        val intent = Intent(context, RecordingService::class.java).apply {
            action = RecordingService.ACTION_STOP
        }
        context.startService(intent)
        timerJob?.cancel()
        _uiState.update { it.copy(visitStatus = PatientDetailVisitStatus.Idle) }
    }

    // Temporary Timer Logic for the MVP
    private var timerJob: Job? = null
    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var seconds = 0
            while (isActive) {
                delay(1000)
                seconds++
                val minutes = seconds / 60
                val secs = seconds % 60
                _uiState.update {
                    it.copy(recordingDuration = String.format("%02d:%02d", minutes, secs))
                }
            }
        }
    }

}