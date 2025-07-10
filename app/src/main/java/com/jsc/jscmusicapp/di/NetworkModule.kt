package com.jsc.jscmusicapp.di

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.jsc.jscmusicapp.network.SaavnApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object NetworkModule {

    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val apiService: SaavnApiService by lazy {

        Retrofit.Builder()
            .baseUrl("https://saavn.dev/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(SaavnApiService::class.java)
    }

}
