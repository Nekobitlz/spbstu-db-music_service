package ru.spbstu.musicservice

import android.app.Application
import android.os.StrictMode
import com.facebook.drawee.backends.pipeline.Fresco
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MusicApp : Application() {

    override fun onCreate() {
        super.onCreate()
        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        Fresco.initialize(applicationContext)
    }
}