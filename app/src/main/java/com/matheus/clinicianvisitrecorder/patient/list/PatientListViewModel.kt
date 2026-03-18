package com.matheus.clinicianvisitrecorder.patient.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.matheus.clinicianvisitrecorder.domain.usecase.GetPatientsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PatientListViewModel @Inject constructor(
    private val getPatientsUseCase: GetPatientsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PatientListState())
    val state = _state.asStateFlow()

    val pagingData = getPatientsUseCase()
        .cachedIn(viewModelScope)

    fun handleIntent(intent: PatientListIntent) {
        when (intent) {
            is PatientListIntent.Refresh -> {
                _state.update { it.copy(isRefreshing = true) }
            }

            is PatientListIntent.OnPatientClick -> {
                // Navigation logic usually happens in the Composable
                // via a callback to the Activity/NavController
            }
        }
    }
}