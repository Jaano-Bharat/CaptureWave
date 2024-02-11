package com.afi.capturewave.ui.component

import android.view.SoundEffectConstants
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import com.afi.capturewave.R

@Composable
fun ConfirmButton(
    text: String = stringResource(R.string.confirm), enabled: Boolean = true, onClick: () -> Unit
) {
    TextButton(onClick = onClick, enabled = enabled) {
        Text(text)
    }
}

@Composable
fun DismissButton(text: String = stringResource(R.string.dismiss), onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(text)
    }
}

@Composable
fun DialogButton(
    text: String, onClick: () -> Unit
) {
    val view = LocalView.current
    TextButton(onClick = {
        view.playSoundEffect(SoundEffectConstants.CLICK)
        onClick.invoke()
    }) {
        Text(text)
    }
}