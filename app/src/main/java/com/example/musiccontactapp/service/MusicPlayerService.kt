package com.example.musiccontactapp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import android.app.Notification.MediaStyle

import com.example.musiccontactapp.R
import com.example.musiccontactapp.presentation.viewmodels.MusicViewModel

import android.content.Context
import android.content.SharedPreferences




class MusicPlayerService : Service() {

    private val binder = MusicBinder()
    private var mediaPlayer: MediaPlayer? = null

    var isPaused = false
        private set

    var currentPath: String? = null

    private val playlist = mutableListOf<String>()
    private var currentIndex = 0

    inner class MusicBinder : Binder() {
        fun getService(): MusicPlayerService = this@MusicPlayerService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification("Ready"))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            MusicPlayerService.ACTION_PLAY -> {
                currentPath?.let { play(it) } ?: run {
                    if (playlist.isNotEmpty()) play(playlist[currentIndex])
                }
            }
            MusicPlayerService.ACTION_PLAY_PATH -> {
                val path = intent.getStringExtra(EXTRA_PATH)
                if (path != null) {
                    play(path)
                }
            }
            MusicPlayerService.ACTION_PAUSE -> pause()
            MusicPlayerService.ACTION_RESUME -> resume()
            MusicPlayerService.ACTION_STOP -> {
                stopForeground(true)
                stopSelf()
            }
            MusicPlayerService.ACTION_NEXT -> skipToNext()
            MusicPlayerService.ACTION_PREVIOUS -> skipToPrevious()
        }
        return START_STICKY
    }

    fun play(path: String) {
        currentPath = path
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(path)
            prepare()
            start()
            isPaused = false
            setOnCompletionListener {
                skipToNext()
            }
        }
        updateNotification("Playing: ${path.substringAfterLast('/')}")
    }

    fun pause() {
        mediaPlayer?.pause()
        isPaused = true
        updateNotification("Paused")
    }

    fun resume() {
        mediaPlayer?.start()
        isPaused = false
        updateNotification("Playing: ${currentPath?.substringAfterLast('/')}")
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    fun getCurrentPosition(): Int = mediaPlayer?.currentPosition ?: 0

    fun getDuration(): Int = mediaPlayer?.duration ?: 0

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }

    fun setPlaylist(list: List<String>) {
        playlist.clear()
        playlist.addAll(list)
        currentIndex = 0
    }

    private fun skipToNext() {
        if (playlist.isEmpty()) return
        currentIndex = (currentIndex + 1) % playlist.size
        play(playlist[currentIndex])
    }

    private fun skipToPrevious() {
        if (playlist.isEmpty()) return
        currentIndex = if (currentIndex - 1 < 0) playlist.size - 1 else currentIndex - 1
        play(playlist[currentIndex])
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Player Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(content: String): Notification {
        val playPauseIntent = if (isPaused) getPendingIntent(MusicPlayerService.ACTION_RESUME) else getPendingIntent(
            MusicPlayerService.ACTION_PAUSE)
        val nextIntent = getPendingIntent(MusicPlayerService.ACTION_NEXT)
        val prevIntent = getPendingIntent(MusicPlayerService.ACTION_PREVIOUS)
        val stopIntent = getPendingIntent(MusicPlayerService.ACTION_STOP)

        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, CHANNEL_ID)
        } else {
            Notification.Builder(this)
        }

        return builder
            .setContentTitle("Music Player")
            .setContentText(content)
            .setSmallIcon(R.drawable.music)
            .setOngoing(true)
            .addAction(Notification.Action.Builder(
                android.R.drawable.ic_media_previous,
                "Previous",
                prevIntent).build())
            .addAction(Notification.Action.Builder(
                if (isPaused) android.R.drawable.ic_media_play else android.R.drawable.ic_media_pause,
                if (isPaused) "Play" else "Pause",
                playPauseIntent).build())
            .addAction(Notification.Action.Builder(
                android.R.drawable.ic_media_next,
                "Next",
                nextIntent).build())
            .setStyle(android.app.Notification.MediaStyle().setShowActionsInCompactView(0, 1, 2))
            .setDeleteIntent(stopIntent)
            .build()
    }

    private fun updateNotification(content: String) {
        val notification = createNotification(content)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }

    private fun getPendingIntent(action: String): PendingIntent {
        val intent = Intent(this, MusicPlayerService::class.java).apply {
            this.action = action
        }
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        else
            PendingIntent.FLAG_UPDATE_CURRENT

        return PendingIntent.getService(this, action.hashCode(), intent, flags)
    }

    companion object {
        const val CHANNEL_ID = "music_channel"
        const val NOTIFICATION_ID = 1
        const val ACTION_PLAY = "com.example.musiccontactapp.ACTION_PLAY"
        const val ACTION_PLAY_PATH = "com.example.musiccontactapp.ACTION_PLAY_PATH"
        const val ACTION_PAUSE = "com.example.musiccontactapp.ACTION_PAUSE"
        const val ACTION_RESUME = "com.example.musiccontactapp.ACTION_RESUME"
        const val ACTION_STOP = "com.example.musiccontactapp.ACTION_STOP"
        const val ACTION_NEXT = "com.example.musiccontactapp.ACTION_NEXT"
        const val ACTION_PREVIOUS = "com.example.musiccontactapp.ACTION_PREVIOUS"
        const val EXTRA_PATH = "extra_path"
    }
}