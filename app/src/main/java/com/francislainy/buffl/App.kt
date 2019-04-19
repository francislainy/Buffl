package com.francislainy.buffl

import android.app.Application
import android.content.Context
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        sInstance = this

        Timber.plant(Utils.MyDebugTree())
    }

    companion object {

        private var sInstance: App? = null

        val context: Context
            get() = sInstance!!.applicationContext
    }

}
