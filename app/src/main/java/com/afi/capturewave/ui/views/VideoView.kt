package com.afi.capturewave.ui.views

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView

@SuppressLint("OpaqueUnitKey")
@Composable
fun VideoView(videoUri: Uri) {
    val context = LocalContext.current

    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context)
            .setUsePlatformDiagnostics(false)
            .build()
            .also { exoPlayer ->
                val mediaItem = MediaItem.Builder()
                    .setUri(videoUri)
                    .build()
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.prepare()
                exoPlayer.playWhenReady = true
            }
    }
    DisposableEffect(
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                StyledPlayerView(context).apply {
                    player = exoPlayer
                    setShowNextButton(false)
                    setShowPreviousButton(false)
                }
            }
        )
    ) {
        onDispose { exoPlayer.release() }
    }
}