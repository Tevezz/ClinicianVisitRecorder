package com.matheus.clinicianvisitrecorder.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaRecorder
import android.os.Environment
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.matheus.clinicianvisitrecorder.domain.model.Visit
import com.matheus.clinicianvisitrecorder.domain.repository.VisitRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class RecordingService : Service() {

    @Inject
    lateinit var visitRepository: VisitRepository

    private var mediaRecorder: MediaRecorder? = null
    private var currentPatientId: String? = null
    private var startTime: Long = 0L
    private var outputFile: File? = null

    private val channelId = "recording_channel"
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "ACTION_START" -> {
                val id = intent.getStringExtra("PATIENT_ID") ?: "unknown"
                currentPatientId = id
                startTime = System.currentTimeMillis()
                startRecording(id)
            }

            "ACTION_STOP" -> stopRecording()
        }
        return START_STICKY
    }

    private fun startRecording(patientId: String) {
        createNotificationChannel()

        val fileName = "visit_${patientId}_${System.currentTimeMillis()}.mp3"
        outputFile = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), fileName)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Visit Recording Active")
            .setContentText("Recording encounter for Patient $patientId")
            .setSmallIcon(android.R.drawable.ic_btn_speak_now) // Use a system icon for now
            .setOngoing(true)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .build()

        startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE)

        try {
            mediaRecorder = (MediaRecorder(this)).apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(outputFile?.absolutePath)
                prepare()
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            stopSelf()
        }
    }

    private fun stopRecording() {
        val patientId = currentPatientId ?: "unknown"
        val duration = calculateDuration()
        val finalPath = outputFile?.absolutePath

        mediaRecorder?.apply {
            try {
                stop()
            } catch (e: IllegalStateException) {

            } finally {
                release()
            }
        }
        mediaRecorder = null

        serviceScope.launch {
            if (finalPath != null) {
                visitRepository.saveVisit(
                    Visit(
                        patientId = patientId,
                        filePath = finalPath,
                        timestamp = System.currentTimeMillis(),
                        duration = duration
                    )
                )
            }

            withContext(Dispatchers.Main) {
                startTime = 0L
                currentPatientId = null
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
    }

    private fun calculateDuration(): String {
        if (startTime == 0L) return "00:00"
        val elapsedMillis = System.currentTimeMillis() - startTime
        val seconds = (elapsedMillis / 1000) % 60
        val minutes = (elapsedMillis / (1000 * 60)) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            channelId,
            "Clinical Recordings",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Used for ongoing medical visit recordings"
        }
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}