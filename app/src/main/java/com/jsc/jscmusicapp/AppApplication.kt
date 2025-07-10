package com.jsc.jscmusicapp

import android.app.Application
import android.content.Context
import com.jsc.jscmusicapp.di.DatabaseModule


class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        DatabaseModule.init(this)
    }

    companion object {
        private lateinit var instance: AppApplication

        fun getContext(): Context {
            if (!::instance.isInitialized) {
                throw IllegalStateException("AppApplication not initialized")
            }
            return instance
        }
    }
}