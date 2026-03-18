package com.matheus.clinicianvisitrecorder.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.os.IBinder
import androidx.core.app.NotificationCompat
import java.io.File

class RecordingService : Service() {

    private var mediaRecorder: MediaRecorder? = null
    private val channelId = "recording_channel"
    private var outputFile: File? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "ACTION_START" -> startRecording(intent.getStringExtra("PATIENT_ID") ?: "unknown")
            "ACTION_STOP" -> stopRecording()
        }
        return START_STICKY
    }

    private fun startRecording(patientId: String) {
        createNotificationChannel()

        // 1. Create the File
        val fileName = "visit_${patientId}_${System.currentTimeMillis()}.mp3"
        outputFile = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), fileName)

        // 2. Setup Notification (Required for Foreground)
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Visit Recording Active")
            .setContentText("Recording encounter for Patient $patientId")
            .setOngoing(true)
            .build()

        startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE)

        // 3. Setup MediaRecorder
        mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(this)
        } else {
            MediaRecorder()
        }.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFile?.absolutePath)
            prepare()
            start()
        }
    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()

        // TODO: Broadcast 'outputFile?.absolutePath' to ViewModel or Database
    }

    private fun createNotificationChannel() {
        val channel =
            NotificationChannel(channelId, "Recordings", NotificationManager.IMPORTANCE_LOW)
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}