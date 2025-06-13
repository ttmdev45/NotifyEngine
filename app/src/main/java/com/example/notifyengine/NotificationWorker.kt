package com.example.notifyengine


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters


//class NotificationWorker(private val context: Context, workerParams: WorkerParameters) :
//    Worker(context, workerParams) {
//
//    override fun doWork(): Result {
//        try {
//            showNotification("Background Work", "WorkManager task completed!")
//            return  Result.success()
//        }catch (e: Exception){
//            return Result.failure()
//        }
//
//    }
//
//    private fun showNotification(title: String, message: String) {
//        val channelId = "background_work_channel"
//        val manager =
//            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        // Create channel for Android 8+
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                channelId,
//                "Background Work Channel",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            manager.createNotificationChannel(channel)
//        }
//
//        val notification = NotificationCompat.Builder(applicationContext, channelId)
//            .setContentTitle(title)
//            .setContentText(message)
//            .setSmallIcon(R.drawable.ic_notification)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setAutoCancel(true)
//            .build()
//
//        manager.notify(1, notification)
//    }
//}


class NotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        showNotification("Reminder", "This is your 5-minute notification!")
        return Result.success()
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "notify_channel"
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create Notification Channel (required for Android O+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "5 Min Notifications", NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        manager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}
