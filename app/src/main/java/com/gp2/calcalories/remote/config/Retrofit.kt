package com.gp2.calcalories.remote.config

import com.gp2.calcalories.BuildConfig
import com.gp2.calcalories.common.base.App
import com.gp2.calcalories.common.preference.UserPreferences
import com.gp2.calcalories.common.util.Alert
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.GeneralSecurityException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

const val APP_NAME = "CalCalories"

//const val BASE_URL = "http://10.0.2.2:8000/api/app/"
//const val BASE_URL = "http://cal.cvs-app.com/api/app/"
const val BASE_URL = "http://calcalories.me/api/app/"

class AppInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("projectName", APP_NAME)
            .addHeader("Authorization", "Bearer ${App.context?.let { UserPreferences.getInstance(it).getUser()?.token }}")
            .method(chain.request().method, chain.request().body)
            .build()
        Alert.log("Header", request.headers["Authorization"].toString())
        return chain.proceed(request)
    }
}

/*
    In kotlin, object declarations are used to declare singleton objects. Singleton pattern ensures that one,
    and only one, instance of an object is created, has one global point of access to that object.
    Object declaration's initialization is thread-safe and done at first access.
 */
object Retrofit {

    private fun configureClient(client: OkHttpClient.Builder): OkHttpClient.Builder {
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            client.sslSocketFactory(sslSocketFactory, (trustAllCerts[0] as X509TrustManager))
            client.hostnameVerifier { _: String?, _: SSLSession? -> true }
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
        }
        return client
    }

    private var logging = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG)
            setLevel(HttpLoggingInterceptor.Level.BODY)
        else setLevel(HttpLoggingInterceptor.Level.NONE)
    }

    private val client = configureClient(OkHttpClient.Builder()).apply {
        retryOnConnectionFailure(true)

        writeTimeout(5, TimeUnit.MINUTES)
        connectTimeout(2, TimeUnit.MINUTES)
        readTimeout(2, TimeUnit.MINUTES)

        addInterceptor(logging)
        addInterceptor(AppInterceptor())

    }.build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    // You make this lazy initialization, to make sure it is initialized at its first usage.
    val API: AppApi by lazy { retrofit.create(AppApi::class.java) }
}