package com.matheus.clinicianvisitrecorder.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import com.matheus.clinicianvisitrecorder.ui.color.ActiveGreen
import com.matheus.clinicianvisitrecorder.ui.color.CardSurface
import com.matheus.clinicianvisitrecorder.ui.color.DarkBlue
import com.matheus.clinicianvisitrecorder.ui.color.Gray
import com.matheus.clinicianvisitrecorder.ui.color.Outline
import com.matheus.clinicianvisitrecorder.ui.color.PurpleWaveform
import com.matheus.clinicianvisitrecorder.ui.color.RecordingRed
import com.matheus.clinicianvisitrecorder.ui.color.StatusActive
import com.matheus.clinicianvisitrecorder.ui.color.White

private val AppColorScheme = darkColorScheme(
    primary = PurpleWaveform,
    onPrimary = White,
    secondary = StatusActive,
    onSecondary = ActiveGreen,
    background = DarkBlue,
    onBackground = White,
    surface = CardSurface,
    onSurface = White,
    surfaceVariant = CardSurface,
    onSurfaceVariant = Gray,
    error = RecordingRed,
    onError = White,
    outline = Outline,
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
