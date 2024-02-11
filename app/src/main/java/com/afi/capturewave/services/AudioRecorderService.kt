package com.afi.capturewave.services

import android.media.MediaRecorder
import android.widget.Toast
import com.afi.capturewave.App
import com.afi.capturewave.R
import com.afi.capturewave.enums.AudioChannels
import com.afi.capturewave.enums.AudioDeviceSource
import com.afi.capturewave.item.AudioFormat
import com.afi.capturewave.util.PlayerHelper
import com.afi.capturewave.util.Preferences

class AudioRecorderService : RecorderService() {
    override val notificationTitle: String
        get() = getString(R.string.recording_audio)

    override fun start() {
        val audioFormat = AudioFormat.getCurrent()

        recorder = PlayerHelper.newRecorder(this).apply {
            Preferences.prefs.getInt(
                Preferences.audioDeviceSourceKey, AudioDeviceSource.DEFAULT.value
            ).let {
                setAudioSource(it)
            }
            if (audioFormat.codec != MediaRecorder.AudioEncoder.OPUS) {
                Preferences.prefs.getInt(Preferences.audioSampleRateKey, -1).takeIf { it > 0 }
                    ?.let {
                        setAudioSamplingRate(it)
                        setAudioEncodingBitRate(it * 32 * 2)
                    }
                Preferences.prefs.getInt(Preferences.audioBitrateKey, -1).takeIf { it > 0 }?.let {
                    setAudioEncodingBitRate(it)
                }
            }

            Preferences.prefs.getInt(Preferences.audioChannelsKey, AudioChannels.MONO.value).let {
                setAudioChannels(it)
            }

            setOutputFormat(audioFormat.format)
            setAudioEncoder(audioFormat.codec)

            outputFile = (application as App).fileRepository.getOutputFile(
                audioFormat.extension
            )
            if (outputFile == null) {
                Toast.makeText(
                    this@AudioRecorderService,
                    R.string.cant_access_selected_folder,
                    Toast.LENGTH_LONG
                ).show()
                onDestroy()
                return
            }

            fileDescriptor = contentResolver.openFileDescriptor(outputFile!!.uri, "w")
            setOutputFile(fileDescriptor?.fileDescriptor)

            runCatching {
                prepare()
            }

            start()
        }

        super.start()
    }

    override fun getCurrentAmplitude() = recorder?.maxAmplitude
}