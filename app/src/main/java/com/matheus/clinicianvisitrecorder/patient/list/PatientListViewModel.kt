package com.matheus.clinicianvisitrecorder.patient.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.matheus.clinicianvisitrecorder.domain.usecase.GetPatientsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientListViewModel @Inject constructor(
    getPatientsUseCase: GetPatientsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        PatientListState(pagingData = getPatientsUseCase().cachedIn(viewModelScope))
    )
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<PatientListEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun handleIntent(intent: PatientListIntent) {
        when (intent) {
            is PatientListIntent.SelectPatient -> viewModelScope.launch {
                _events.send(PatientListEvent.NavigateToDetail(intent.patientId))
            }
        }
    }
}
