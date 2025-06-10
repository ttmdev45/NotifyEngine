package com.example.notifyengine

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MyForegroundService : Service() {

    private val CHANNEL_ID = "foreground_service_channel"
    private val SECONDARY_CHANNEL_ID = "secondary_notification_channel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Service Running")
            .setContentText("Background service is active")
            .setSmallIcon(R.drawable.ic_notification)
            .build()

        startForeground(1, notification)

        // Simulate background work
        Thread {
            Thread.sleep(5000)
            showAnotherNotification()
            stopSelf()
        }.start()

        return START_NOT_STICKY
    }

    private fun showAnotherNotification() {
        val notification = NotificationCompat.Builder(this, SECONDARY_CHANNEL_ID)
            .setContentTitle("Background Task Done")
            .setContentText("Task in background service is completed")
            .setSmallIcon(R.drawable.ic_notification)
            .build()

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(2, notification)
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val secondaryChannel = NotificationChannel(
                SECONDARY_CHANNEL_ID,
                "Secondary Channel",
                NotificationManager.IMPORTANCE_HIGH
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
            manager.createNotificationChannel(secondaryChannel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
