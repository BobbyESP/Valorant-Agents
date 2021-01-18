package com.ahmedvargos.remote.di

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val loggerName = "LOGGER"
fun getRemoteModule(baseUrl: String, isDebugBuild: Boolean) = module {
    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl(baseUrl)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    factory {
        GsonBuilder()
            .setLenient()
            .create()
    }

    if (isDebugBuild)
        factory<Interceptor>(named(loggerName)) {
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        }

    factory { OkHttpClient.Builder().addInterceptor(get(qualifier = named(loggerName))).build() }

}