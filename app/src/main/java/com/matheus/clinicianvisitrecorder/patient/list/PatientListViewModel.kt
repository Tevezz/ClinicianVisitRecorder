package com.matheus.clinicianvisitrecorder.patient.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.matheus.clinicianvisitrecorder.domain.usecase.GetPatientsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PatientListViewModel @Inject constructor(
    getPatientsUseCase: GetPatientsUseCase
) : ViewModel() {

    val pagingData = getPatientsUseCase()
        .cachedIn(viewModelScope)
}