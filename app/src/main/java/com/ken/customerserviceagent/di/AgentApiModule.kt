package com.ken.customerserviceagent.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.ken.customerserviceagent.api.AgentApi
import com.ken.customerserviceagent.api.handler.ZonedDateTimeHandler
import com.ken.customerserviceagent.data.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AgentApiModule {
    @Provides
    @Singleton
    fun provideAgentApi(app : Application, sharedPreferences: SharedPreferences) : AgentApi {
        val cacheSize = (10 * 1024 * 1024).toLong()
        val cache = Cache(app.cacheDir, cacheSize)
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .cache(cache)
            .addInterceptor {
                var request = it.request()
                    .newBuilder()
                    .addHeader(
                        "X-Branch-Auth-Token",
                        sharedPreferences.getString(Constants.AUTH_TOKEN, "") ?: ""
                    )
                    .build()
                request = if (hasNetwork(app) == true)
                    request.newBuilder()
                        .header("Cache-Control","public, max-age=" + 5)
                        .build()
                else
                    request.newBuilder()
                        .header("Cache-Control","public, only-if-cached, max-stale="+ 60 * 60 * 24 * 7)
                        .build()
                it.proceed(request)
            }
            .addInterceptor(logging)
            .build()

        val gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(ZonedDateTimeHandler::class.java, ZonedDateTimeHandler())
            .create()

        return Retrofit.Builder()
            .baseUrl("https://android-messaging.branch.co/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
            .create(AgentApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application): SharedPreferences {
        return app.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    private fun hasNetwork(app : Application): Boolean? {
        var isConnected: Boolean? = false
        val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if ((activeNetwork != null) && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }
}