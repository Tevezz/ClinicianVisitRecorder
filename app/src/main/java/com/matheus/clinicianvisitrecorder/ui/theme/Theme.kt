package com.matheus.clinicianvisitrecorder.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import com.matheus.clinicianvisitrecorder.ui.color.CardSurface
import com.matheus.clinicianvisitrecorder.ui.color.DarkBlue
import com.matheus.clinicianvisitrecorder.ui.color.PurpleWaveform
import com.matheus.clinicianvisitrecorder.ui.color.RecordingRed

private val AppColorScheme = darkColorScheme(
    primary = PurpleWaveform,
    background = DarkBlue,
    surface = CardSurface,
    error = RecordingRed,
)

@Composable
fun ClinicianVisitRecorderTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        content = content
    )
}
