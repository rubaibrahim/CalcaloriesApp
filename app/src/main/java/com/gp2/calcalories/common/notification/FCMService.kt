package com.gp2.calcalories.common.notification

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.gp2.calcalories.remote.config.APP_NAME
import com.gp2.calcalories.common.preference.UserPreferences
import com.gp2.calcalories.common.util.Alert


class FCMService : FirebaseMessagingService() {
    private var session: UserPreferences? = null

    override fun onCreate() {
        super.onCreate()
        session = UserPreferences.getInstance(this)


        if (session?.getFirebaseToken().isNullOrEmpty()) {
            getFireBaseToken()
        }
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Alert.log("FCM onNewToken", s)
        session?.saveFirebaseToken(s)
    }

    private fun getFireBaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String?> ->
            try {
                if (!task.isSuccessful) {
                    Log.w(APP_NAME, "getInstanceId failed", task.exception)
                    return@addOnCompleteListener
                }
                // Get new Instance ID token
                session?.saveFirebaseToken(task.result)
                Alert.log("FCM onComplete", session?.getFirebaseToken().toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        try {
            Alert.log("FCM onMessage", remoteMessage.data.toString())

            if (remoteMessage.data.isNotEmpty() &&
                remoteMessage.data.containsKey("app") &&
                remoteMessage.data.containsKey("type")
            ) {

                val app = remoteMessage.data["app"]
                val type = remoteMessage.data["type"]

                if (app != null && app == APP_NAME && type != null)
                    when (type) {
                        "support" -> {
                            if (remoteMessage.data["messageType"].toString() == "2")
                                Thread.sleep(10 * 1000)
                            NotificationUtils.viewNotification(this,
                                remoteMessage.data["title"],
                                remoteMessage.data["message"]
                            )
                        }
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}