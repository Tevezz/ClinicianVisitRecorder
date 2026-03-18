package com.matheus.clinicianvisitrecorder.patient.detail

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.core.content.ContextCompat
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

    private var mediaPlayer: MediaPlayer? = null

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

    fun playRecording(filePath: String) {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(filePath)
                prepare()
                start()
            }
        } catch (e: Exception) {

        }
    }

    private fun startRecording() {
        val intent = Intent(context, RecordingService::class.java).apply {
            action = "ACTION_START"
            putExtra("PATIENT_ID", patientId)
        }

        ContextCompat.startForegroundService(context, intent)

        _uiState.update {
            it.copy(
                visitStatus = PatientDetailVisitStatus.Active,
                recordingDuration = "00:00",
                transcript = "Waiting for audio..."
            )
        }

        startTimer()
    }

    private fun stopRecording() {
        val intent = Intent(context, RecordingService::class.java).apply {
            action = "ACTION_STOP"
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

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}