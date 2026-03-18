package com.matheus.clinicianvisitrecorder.patient

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientListScreen(
    onNavigateDetail: (String) -> Unit,
    viewModel: PatientListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val patients = viewModel.pagingData.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.title, color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0D1117))
            )
        },
        containerColor = Color(0xFF0D1117) // Nestmed Dark Theme
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {

            // Handle initial load
            if (patients.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Cyan
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        count = patients.itemCount,
                        key = patients.itemKey { it.id }
                    ) { index ->
                        val patient = patients[index]
                        if (patient != null) {
                            PatientCard(patient = patient) {
                                onNavigateDetail(patient.id)
                            }
                        }
                    }

                    // Loading indicator for pagination (scrolling to bottom)
                    if (patients.loadState.append is LoadState.Loading) {
                        item {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .wrapContentWidth(Alignment.CenterHorizontally),
                                color = Color.Cyan
                            )
                        }
                    }
                }
            }
        }
    }
}