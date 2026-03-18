package com.matheus.clinicianvisitrecorder.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.matheus.clinicianvisitrecorder.data.remote.RickAndMortyApi
import com.matheus.clinicianvisitrecorder.domain.model.Patient
import toPatient

internal class PatientDataSource(
    private val api: RickAndMortyApi
) : PagingSource<Int, Patient>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Patient> {
        val page = params.key ?: 1
        return try {
            val response = api.getCharacters(page)
            LoadResult.Page(
                data = response.results.map { it.toPatient() },
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.info.next == null) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Patient>): Int? = state.anchorPosition
}
