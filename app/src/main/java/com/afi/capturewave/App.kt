package com.afi.capturewave

import android.app.Application
import com.afi.capturewave.util.FileRepository
import com.afi.capturewave.util.FileRepositoryImpl
import com.afi.capturewave.util.NotificationHelper
import com.afi.capturewave.util.Preferences
import com.afi.capturewave.util.ShortcutHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class App : Application() {
    val fileRepository: FileRepository by lazy {
        FileRepositoryImpl(this)
    }

    override fun onCreate() {
        super.onCreate()
        applicationScope = CoroutineScope(SupervisorJob())
        Preferences.init(this)
        NotificationHelper.buildNotificationChannels(this)
        ShortcutHelper.createShortcuts(this)
    }

    companion object {
        lateinit var applicationScope: CoroutineScope
    }
}