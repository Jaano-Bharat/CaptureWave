package com.afi.capturewave.ui.models

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.afi.capturewave.App
import com.afi.capturewave.enums.SortOrder
import com.afi.capturewave.item.RecordingItemData
import com.afi.capturewave.util.FileRepository
import com.afi.capturewave.util.PlayerHelper
import kotlinx.coroutines.launch
import java.io.IOException

class PlayerModel(private val fileRepository: FileRepository) : ViewModel() {
    var isPlaying by mutableStateOf(false)
    var player by mutableStateOf<MediaPlayer?>(null)
    var currentPlayingFile by mutableStateOf<DocumentFile?>(null)

    var selectedFiles by mutableStateOf(listOf<RecordingItemData>())

    private var sortOrder = SortOrder.DEFAULT

    var audioRecordingItems by mutableStateOf(listOf<RecordingItemData>())
    var screenRecordingItems by mutableStateOf(listOf<RecordingItemData>())

    init {
        loadFiles()
    }

    fun loadFiles() {
        viewModelScope.launch {
            audioRecordingItems = fileRepository.getAudioRecordingItems(sortOrder)
            screenRecordingItems = fileRepository.getVideoRecordingItems(sortOrder)
        }
    }

    fun sortItems(newSortOrder: SortOrder) {
        if (newSortOrder == sortOrder) return
        sortOrder = newSortOrder
        loadFiles()
    }

    fun deleteFiles() {
        viewModelScope.launch {
            if (selectedFiles.isEmpty()) {
                fileRepository.deleteAllFiles()
                loadFiles()
                return@launch
            }
            fileRepository.deleteFiles(selectedFiles.map { it.recordingFile })
            selectedFiles = emptyList()
            loadFiles()
        }
    }

    fun startPlaying(context: Context, file: DocumentFile) {
        stopPlaying()

        currentPlayingFile = file
        player = getMediaPlayer().apply {
            try {
                context.contentResolver.openFileDescriptor(file.uri, "r")?.use {
                    setDataSource(it.fileDescriptor)
                }
                prepare()
                start()
            } catch (e: IOException) {
                Log.e("reading file", e.toString())
            }
        }
        player?.setOnCompletionListener {
            stopPlaying()
        }
        isPlaying = true
    }

    fun stopPlaying() {
        currentPlayingFile = null
        player?.release()
        player = null
        isPlaying = false
    }

    fun pausePlaying() {
        player?.pause()
        isPlaying = false
    }

    fun resumePlaying() {
        player?.start()
        isPlaying = true
    }

    private fun getMediaPlayer(): MediaPlayer {
        return MediaPlayer().apply {
            setAudioAttributes(PlayerHelper.getAudioAttributes())
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as App)
                PlayerModel(application.fileRepository)
            }
        }
    }
}
