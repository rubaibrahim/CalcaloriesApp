package com.gp2.calcalories.common.preference

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.gp2.calcalories.remote.model.user.entity.User

class UserPreferences private constructor(context: Context) {
    private val ref: SharedPreferences =
        context.getSharedPreferences("SettingsRef", Context.MODE_PRIVATE)

    companion object {

        @Volatile
        private var INSTANCE: UserPreferences? = null
        fun getInstance(context: Context): UserPreferences {
            return INSTANCE ?: synchronized(    this) {
                val instance = UserPreferences(context)
                INSTANCE = instance
                return instance
            }
        }
    }

    //  Users data
    fun saveUser(user: User?) {
        val editor: SharedPreferences.Editor = ref.edit()

        editor.putString("UserData", if (user != null) Gson().toJson(user) else null)
        editor.apply()
    }

    fun getUser(): User? {
        val data: String = ref.getString("UserData", "").toString()
        return if (data == "") null else Gson().fromJson(data, User::class.java)
    }

    //  Firebase Token
    fun saveFirebaseToken(token: String?) {
        val editor: SharedPreferences.Editor = ref.edit()
        editor.putString("FirebaseToken", token)
        editor.apply()
    }

    fun getFirebaseToken(): String {
        return ref.getString("FirebaseToken", "").toString()
    }


    fun saveTest(TestKey: String?) {
        val editor: SharedPreferences.Editor = ref.edit()
        editor.putString("TestKey", TestKey)
        editor.apply()
    }

    fun getTest(): String {
        return ref.getString("TestKey", "").toString()
    }


    //  get auto notification id
    fun getAutoNotificationId(): Int {
        val id: Int = ref.getInt("AutoNotificationId", 0) + 1
        val editor: SharedPreferences.Editor = ref.edit()
        editor.putInt("AutoNotificationId", id)
        editor.apply()
        return id
    }

}