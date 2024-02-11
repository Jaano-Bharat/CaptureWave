package com.afi.capturewave.services

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import com.afi.capturewave.ui.MainActivity

@RequiresApi(Build.VERSION_CODES.N)
class AudioRecorderTile : TileService() {
    @SuppressLint("StartActivityAndCollapseDeprecated")
    override fun onClick() {
        super.onClick()

        val intent = Intent(this, MainActivity::class.java).putExtra("action", "audio").apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivityAndCollapse(intent)
    }
}