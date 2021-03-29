package com.example.githubuserbfaa

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val TYPE_REPEAT = "RepeatingAlarm"
        const val EXTRA_TYPE = "type"
        const val EXTRA_MESSAGE = "message"

        private const val ID_REPEATING = 101
        private const val TIME_ALARM = "09:00"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_TYPE)

        val notifId =
            if (type.equals(TYPE_REPEAT, ignoreCase = true)) ID_REPEATING else return
        val message = intent.getStringExtra(EXTRA_MESSAGE) as String

        showAlarmNotification(context, message, notifId)
    }

    private fun showAlarmNotification(context: Context, message: String, notifId: Int) {
        val CHANNEL_ID = "Channel_1"
        val CHANNEL_NAME = "AlarmManager channel"
        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntentActivity = PendingIntent.getActivity(context, ID_REPEATING, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_alarm_24)
            .setContentTitle("GitHubUser BFAA")
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setContentIntent(pendingIntentActivity)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)
    }

    fun setRepeatingAlarm(context: Context, type: String, message: String){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_TYPE, type)

        val timeArray =
            TIME_ALARM.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val calender = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
            set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
            set(Calendar.SECOND, 0)
        }

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, PendingIntent.FLAG_ONE_SHOT)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calender.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        Toast.makeText(context, "Daily Reminder Set Up !", Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm(context: Context, type: String){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = ID_REPEATING
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, "Daily Reminder Cancel", Toast.LENGTH_SHORT).show()
    }
}