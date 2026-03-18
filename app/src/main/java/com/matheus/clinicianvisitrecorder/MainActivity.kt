package com.matheus.clinicianvisitrecorder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.matheus.clinicianvisitrecorder.navigation.Route
import com.matheus.clinicianvisitrecorder.patient.detail.PatientDetailScreen
import com.matheus.clinicianvisitrecorder.patient.list.PatientListScreen
import com.matheus.clinicianvisitrecorder.ui.theme.ClinicianVisitRecorderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClinicianVisitRecorderTheme {

                val backStack = rememberNavBackStack(Route.PatientList)

                NavDisplay(
                    backStack = backStack,
                    onBack = {
                        if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) else finish()
                    },
                    entryProvider = entryProvider {
                        entry<Route.PatientList> {
                            PatientListScreen(
                                onNavigateDetail = { id ->
                                    backStack.add(Route.PatientDetail(id))
                                }
                            )
                        }
                        entry<Route.PatientDetail> { key ->
                            PatientDetailScreen(
                                patientId = key.patientId
                            )
                        }
                    }
                )
            }
        }
    }
}