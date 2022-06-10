package com.gp2.calcalories.common.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.core.ImageTranscoderType
import com.facebook.imagepipeline.core.MemoryChunkType
import com.google.android.gms.security.ProviderInstaller
import com.gp2.calcalories.common.preference.UserPreferences
import com.gp2.calcalories.common.util.Utility
import javax.net.ssl.SSLContext

class App : Application() {
    val preference: UserPreferences by lazy { UserPreferences.getInstance(this) }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
    }


    override fun onCreate() {
        super.onCreate()
        try {
            context = this

            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

//            FirebaseApp.initializeApp(this)
//            FirebaseMessaging.getInstance().subscribeToTopic(Utility.PROJECT_NAME)
            Fresco.initialize(
                this,
                ImagePipelineConfig.newBuilder(this)
                    .setMemoryChunkType(MemoryChunkType.BUFFER_MEMORY)
                    .setImageTranscoderType(ImageTranscoderType.JAVA_TRANSCODER)
                    .experiment().setNativeCodeDisabled(true)
                    .build()
            )


            // Google Play will install latest OpenSSL
            ProviderInstaller.installIfNeeded(applicationContext)
            val sslContext = SSLContext.getInstance("TLSv1.2")
            sslContext.init(null, null, null)
            sslContext.createSSLEngine()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}