package com.gp2.calcalories.common.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.gp2.calcalories.R
import com.gp2.calcalories.common.base.App
import com.gp2.calcalories.common.preference.UserPreferences
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

object Utility {
    fun isInternetAvailable(): Boolean {
        // Returns connection type. 0: none; 1: mobile data; 2: wifi
        var result = 0
        val connectivityManager = App.context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.run {
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
                when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        result = 2
                    }
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        result = 1
                    }
                    hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> {
                        result = 3
                    }
                }
            }
        }
        if (result == 0) {
            App.context?.let {
                Alert.toast(it, R.string.please_check_your_connection_and_try_again)
            }
        }
        Alert.log("isInternetAvailable", result.toString())
        return result != 0
    }

    fun getCurrentDate(formatTo: String? = "EEEE, dd MMMM, yyyy - hh:mm a"): String {
        return try {
            val calendar = Calendar.getInstance()
            val mFormat = SimpleDateFormat(formatTo, Locale.ENGLISH)
            mFormat.format(calendar.time)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Date().time.toString()
        }
    }

    fun askToLoginInAgain(context: Context) {
        // Remove User session
        UserPreferences.getInstance(context).saveUser(null)

        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.sign_in))
            .setMessage(context.getString(R.string.you_must_log_in_to_your_account_first))
            .setPositiveButton(context.getString(R.string.sign_in)) { _: DialogInterface?, _: Int ->
                startAppAgain(context)
            }
            .setNegativeButton(android.R.string.cancel) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .show()
    }

    fun startAppAgain(context: Context) {
        try {
            //RESTART APPLICATION FOR DEPENDENT THE NEW LANGUAGE
            val packageManager = context.packageManager
            val intent = packageManager?.getLaunchIntentForPackage(context.packageName)
            if (intent != null) {
                val componentName = intent.component
                val mainIntent = Intent.makeRestartActivityTask(componentName)
                context.startActivity(mainIntent)
                //System.exit(0);
            }
        } catch (e: Exception) {
            e.printStackTrace()
            exitProcess(0) // System.exit(0)
        }
    }
}