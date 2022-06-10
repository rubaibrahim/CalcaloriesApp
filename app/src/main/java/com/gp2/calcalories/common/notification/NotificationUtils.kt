package com.gp2.calcalories.common.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.gp2.calcalories.R
import com.gp2.calcalories.common.preference.UserPreferences
import com.gp2.calcalories.remote.config.APP_NAME
import com.gp2.calcalories.ui.activities.MainActivity

object NotificationUtils {
    private const val NOTIFICATION_TAG_NOT = APP_NAME + "NewNotification"
    private const val CHANNEL_ID_NOT = APP_NAME + "AppChannel"
    private var CHANNEL_ID = ""

    @JvmStatic
    fun viewNotification(context: Context, title: String?, message: String?) {
        var mTitle = title
        try {
            val res = context.resources
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CHANNEL_ID = CHANNEL_ID_NOT
                createNotificationChannel(context)
            }

            val bitmap = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher)
            if (title != null && title.isEmpty()) mTitle = context.getString(R.string.app_name)

            val intent = Intent(context, MainActivity::class.java)
            val backIntent = Intent(context, MainActivity::class.java)
            backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            val resultPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getActivities(context, 0,
                    arrayOf(backIntent, intent), PendingIntent.FLAG_IMMUTABLE);
            } else {
                PendingIntent.getActivities(context, 0,
                    arrayOf(backIntent, intent), PendingIntent.FLAG_ONE_SHOT
                )
            }
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_ALL)
                .setLights(Color.argb(255, 0, 204, 204), 5000, 5000)
                .setSmallIcon(R.drawable.app_status_bar_icon)
                .setContentTitle(mTitle)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setLargeIcon(bitmap)
                .setTicker(message)
                .setNumber(1)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(message)
                        .setBigContentTitle(mTitle)
                )
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)

                notify(context, UserPreferences.getInstance(context).getAutoNotificationId(), builder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun notify(context: Context, number: Int, notification: Notification) {
        try {
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(NOTIFICATION_TAG_NOT, number, notification)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun cancel(context: Context?, NOTIFICATION_TAG: String?) {
        try {
            val notificationManager = NotificationManagerCompat.from(context!!)
            notificationManager.cancel(NOTIFICATION_TAG, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createNotificationChannel(context: Context) {
        try {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name: CharSequence = APP_NAME + "App"
                val description = " My App Channel to show notification"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(CHANNEL_ID_NOT, name, importance)
                channel.description = description
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                val notificationManager = context.getSystemService(
                    NotificationManager::class.java
                )
                notificationManager?.createNotificationChannel(channel)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}