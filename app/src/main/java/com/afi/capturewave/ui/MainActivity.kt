package com.afi.capturewave.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.afi.capturewave.enums.RecorderType
import com.afi.capturewave.enums.ThemeMode
import com.afi.capturewave.ui.common.Route
import com.afi.capturewave.ui.common.animatedComposable
import com.afi.capturewave.ui.models.ThemeModel
import com.afi.capturewave.ui.pages.home.HomeScreen
import com.afi.capturewave.ui.pages.recordings.PlayerScreen
import com.afi.capturewave.ui.pages.settings.about.AboutPage
import com.afi.capturewave.ui.pages.settings.about.CreditsPage
import com.afi.capturewave.ui.pages.settings.theme.DarkThemePreferences
import com.afi.capturewave.ui.pages.settings.SettingsPage
import com.afi.capturewave.ui.pages.settings.audio_format.AudioFormatPage
import com.afi.capturewave.ui.pages.settings.general.GeneralPage
import com.afi.capturewave.ui.pages.settings.recorder.ScreenRecorderPage
import com.afi.capturewave.ui.theme.CaptureWaveTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v, insets ->
            v.setPadding(0, 0, 0, 0)
            insets
        }
        val themeModel: ThemeModel by viewModels()

        val initialRecorder = when (intent?.getStringExtra("action")) {
            "audio" -> RecorderType.AUDIO
            "screen" -> RecorderType.VIDEO
            else -> RecorderType.NONE
        }

        setContent {
            CaptureWaveTheme(
                when (themeModel.themeMode) {
                    ThemeMode.SYSTEM -> isSystemInDarkTheme()
                    ThemeMode.DARK, ThemeMode.AMOLED -> true
                    else -> false
                }, amoledDark = themeModel.themeMode == ThemeMode.AMOLED
            ) {
                val navController = rememberNavController()

                val onBackPressed: () -> Unit = {
                    with(navController) {
                        if (currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                            popBackStack()
                        }
                    }
                }
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Route.HOME,
                        modifier = Modifier
                    ) {
                        animatedComposable(Route.HOME) {
                            HomeScreen(initialRecorder, navController)
                        }

                        animatedComposable(Route.RECORDING_PLAYER) {
                            PlayerScreen(showVideoModeInitially = false, onBackPressed = onBackPressed)
                        }

                        settingsGraph(
                            navController = navController, onBackPressed = onBackPressed
                        )
                    }
                }
            }
        }
    }
}

fun NavGraphBuilder.settingsGraph(
    navController: NavHostController, onBackPressed: () -> Unit
) {
    navigation(startDestination = Route.SETTINGS_PAGE, route = Route.SETTINGS) {
        animatedComposable(Route.GENERAL) {
            GeneralPage { onBackPressed() }
        }
        animatedComposable(Route.SETTINGS_PAGE) {
            SettingsPage(
                navController = navController, onBackPressed = onBackPressed
            )
        }
        animatedComposable(Route.ABOUT) {
            AboutPage(
                onBackPressed = onBackPressed, navController = navController
            )
        }
        animatedComposable(Route.SCREEN_RECORDER) {
            ScreenRecorderPage(
                onBackPressed = onBackPressed
            )
        }
        animatedComposable(Route.AUDIO_FORMAT) {
            AudioFormatPage(
                onBackPressed = onBackPressed
            )
        }
        animatedComposable(Route.CREDITS) { CreditsPage { onBackPressed() } }
        animatedComposable(Route.THEME) { DarkThemePreferences { onBackPressed() } }
    }
}