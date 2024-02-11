package com.afi.capturewave.enums

import android.media.MediaRecorder
import android.os.Build
import androidx.annotation.RequiresApi

enum class AudioDeviceSource(val value: Int) {
    DEFAULT(MediaRecorder.AudioSource.DEFAULT), MIC(MediaRecorder.AudioSource.MIC), CAMCORDER(
        MediaRecorder.AudioSource.CAMCORDER
    ),

    @RequiresApi(Build.VERSION_CODES.N)
    UNPROCESSED(MediaRecorder.AudioSource.UNPROCESSED);

    companion object {
        fun fromInt(value: Int) = entries.first { it.value == value }
    }
}