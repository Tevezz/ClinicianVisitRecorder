package com.matheus.clinicianvisitrecorder.patient.detail

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.matheus.clinicianvisitrecorder.domain.model.Patient
import com.matheus.clinicianvisitrecorder.domain.model.Visit
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PatientDetailScreen(
    patientId: String,
    viewModel: PatientDetailViewModel = hiltViewModel<PatientDetailViewModel, PatientDetailViewModel.Factory>(
        key = patientId
    ) { factory ->
        factory.create(patientId)
    }
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.handleIntent(PatientDetailIntent.StartVisit)
        } else {
            // TODO: handle permission denial
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        when (state.visitStatus) {
            PatientDetailVisitStatus.Active -> ActiveVisitContent(
                state = state,
                modifier = Modifier.padding(padding),
                onStopClick = { viewModel.handleIntent(PatientDetailIntent.StopVisit) }
            )

            else -> IdleVisitContent(
                state = state,
                modifier = Modifier.padding(padding),
                onStartClick = { permissionLauncher.launch(Manifest.permission.RECORD_AUDIO) },
                onPlayClick = { path -> viewModel.playRecording(path) }
            )
        }
    }
}

@Composable
fun IdleVisitContent(
    state: PatientDetailUiState,
    modifier: Modifier = Modifier,
    onStartClick: () -> Unit,
    onPlayClick: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        PatientHeaderCard(state.patient, isActive = false)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onStartClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9F7AEA)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Mic, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Start New Visit", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(modifier = Modifier.weight(1f)) {
            VisitHistorySection(
                visits = state.visits,
                onPlayClick = { path ->
                    onPlayClick(path)
                }
            )
        }
    }
}

@Composable
fun ActiveVisitContent(
    state: PatientDetailUiState,
    onStopClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(state.transcript) {
        if (state.transcript.isNotEmpty()) {
            listState.animateScrollToItem(index = 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        PatientHeaderCard(state.patient, isActive = true)

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
            border = BorderStroke(1.dp, Color(0xFF30363D))
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = "LIVE TRANSCRIPT",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF9F7AEA) // Purple accent
                    )
                }
                item {
                    Text(
                        text = state.transcript.ifEmpty { "Listening for audio..." },
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (state.transcript.isEmpty()) Color.Gray else Color.White,
                        lineHeight = 28.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = state.recordingDuration, // e.g., "04:20"
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
            WaveformPlaceholder()
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onStopClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("End Visit", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun PatientHeaderCard(
    patient: Patient?,
    isActive: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFF30363D))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                model = patient?.profileImageUrl,
                contentDescription = patient?.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = patient?.name ?: "Loading...",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                val statusColor = when (patient?.condition?.lowercase()) {
                    "alive" -> Color(0xFF4ADE80)
                    "dead" -> Color(0xFFEF4444)
                    else -> Color.Gray
                }
                Text(
                    text = patient?.condition ?: "",
                    color = statusColor,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )

                if (patient?.species?.isNotEmpty() == true) {
                    Text(
                        text = patient.species,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                if (patient?.location?.isNotEmpty() == true) {
                    Text(
                        text = patient.location,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            if (isActive) {
                Surface(
                    color = Color(0xFF238636).copy(alpha = 0.2f),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFF238636))
                ) {
                    Text(
                        text = "Active",
                        color = Color(0xFF4ADE80),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

@Composable
fun WaveformPlaceholder() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val barCount = 15
        val purpleColor = Color(0xFF9F7AEA)

        repeat(barCount) { index ->
            // Simulate random heights for a "live" look
            val heightPercent = when (index % 5) {
                0 -> 0.4f; 1 -> 0.8f; 2 -> 1.0f; 3 -> 0.7f; else -> 0.5f
            }

            Box(
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .width(6.dp)
                    .fillMaxHeight(heightPercent)
                    .clip(RoundedCornerShape(3.dp))
                    .background(purpleColor)
            )
        }
    }
}

@Composable
fun TranscriptCard(transcript: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFF30363D))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Live Transcript",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (transcript.isEmpty()) "\"...waiting for speech...\"" else "\"$transcript\"",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

@Composable
fun VisitHistorySection(
    visits: List<Visit>,
    onPlayClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            Text(
                text = "Previous Visits",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }

        if (visits.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No previous recordings found.",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        } else {
            items(
                items = visits,
                key = { it.id }
            ) { visit ->
                VisitItem(visit, onPlayClick)
            }
        }
    }
}

@Composable
fun VisitItem(visit: Visit, onPlayClick: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        border = BorderStroke(1.dp, Color(0xFF30363D))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.PlayCircle,
                contentDescription = "Play",
                tint = Color(0xFF9F7AEA),
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onPlayClick(visit.filePath) }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Visit on ${formatTimestamp(visit.timestamp)}",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Duration: ${visit.duration}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

fun formatTimestamp(timestamp: Long): String {
    val instant = Instant.ofEpochMilli(timestamp)
    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy • h:mm a")
        .withZone(ZoneId.systemDefault())
        .withLocale(Locale.getDefault())

    return formatter.format(instant)
}