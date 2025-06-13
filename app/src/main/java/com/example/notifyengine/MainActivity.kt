package com.example.notifyengine

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.notifyengine.alarmNoti.AlarmReceiver
import com.example.notifyengine.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        binding.btnStartService.setOnClickListener {
//            val serviceIntent = Intent(this, MyForegroundService::class.java)
//            startForegroundService(serviceIntent)
//        }

        binding.btnStartWork.setOnClickListener {

            // scheduleNotificationWork()
            setAlarmAfterTwoMinutes()
        }

    }

    private fun scheduleNotificationWork() {
        /*** WorkManager is not designed for high-frequency tasks.
         *  For PeriodicWorkRequest, the minimum interval is 15 minutes.**/
        //Constraints
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_ROAMING)
            .setRequiresBatteryNotLow(true)
            .setRequiresStorageNotLow(true)
            .build()

        //Work Request
        //            val workRequest: WorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
        //                .setConstraints(constraints)
        //                .build()
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            5, TimeUnit.MINUTES
        ).setConstraints(constraints)
            .build()

        //Enqueue Work Manager
        WorkManager.getInstance(applicationContext)
            .enqueue(workRequest)


//        WorkManager.getInstance(this)
//            .enqueueUniquePeriodicWork(
//                "five_minute_notification",
//                ExistingPeriodicWorkPolicy.KEEP,
//                workRequest
//            )
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun setAlarmAfterTwoMinutes() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = System.currentTimeMillis() + 2 * 60 * 1000 // 2 minutes in millis

        // Use setExactAndAllowWhileIdle for precision and Doze mode compatibility
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )

        Toast.makeText(this, "Alarm set for 2 minutes later", Toast.LENGTH_SHORT).show()
    }

}