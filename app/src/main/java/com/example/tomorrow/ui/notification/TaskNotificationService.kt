package com.example.tomorrow.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.tomorrow.MainActivity
import com.example.tomorrow.data.Task
import com.example.tomorrow.R

class TaskNotificationService(private val context: Context) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showTaskNotification(task: Task ) {
        val channelId = "task_deadlines"
        createNotificationChannel(channelId)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("task_id", task.id)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            task.id.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("O prazo da sua tarefa está chegando ao fim!")
            .setContentText("'${task.title}' irá terminar amanhã! Termine antes da data expirar!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(task.id.hashCode(), notification)

    }

    private fun createNotificationChannel(channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Tomorrow",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaficações de deadlines chegando."
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
}