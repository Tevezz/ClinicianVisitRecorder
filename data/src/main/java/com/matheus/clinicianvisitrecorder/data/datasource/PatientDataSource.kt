package com.matheus.clinicianvisitrecorder.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.matheus.clinicianvisitrecorder.domain.model.Patient
import kotlinx.coroutines.delay

internal class PatientDataSource : PagingSource<Int, Patient>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Patient> {
        val position = params.key ?: 1

        // Simulate a tiny bit of network lag
        delay(500)

        // Generate 20 fake patients per "page"
        val patients = (1..20).map { i ->
            val id = ((position - 1) * 20 + i).toString()
            Patient(id, "Patient $id", "Chronic Condition $id", "")
        }

        return LoadResult.Page(
            data = patients,
            prevKey = if (position == 1) null else position - 1,
            nextKey = if (patients.isEmpty()) null else position + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Patient>): Int? = state.anchorPosition
}