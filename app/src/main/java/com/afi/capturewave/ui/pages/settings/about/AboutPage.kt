package com.afi.capturewave.ui.pages.settings.about

import android.view.HapticFeedbackConstants
import android.view.SoundEffectConstants
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.TipsAndUpdates
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.afi.capturewave.BuildConfig
import com.afi.capturewave.R
import com.afi.capturewave.ui.common.Route
import com.afi.capturewave.ui.component.BackButton
import com.afi.capturewave.ui.component.CurlyCornerShape


const val telegram = "https://t.me/capturewave"
const val github = "https://github.com/Ashinch/ReadYou"
const val releases = "https://fonts.google.com/icons"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutPage(
    onBackPressed: () -> Unit, navController: NavHostController
) {
    val uriHandler = LocalUriHandler.current
    val view = LocalView.current
    var clickTime by remember { mutableLongStateOf(System.currentTimeMillis() - 2000) }
    var pressAMP by remember { mutableFloatStateOf(16f) }
    val animatedPress by animateFloatAsState(
        targetValue = pressAMP, animationSpec = tween(), label = ""
    )

    fun openUrl(url: String) {
        uriHandler.openUri(url)
    }

    Scaffold(modifier = Modifier, topBar = {
        TopAppBar(
            title = {},
            navigationIcon = {
                BackButton {
                    onBackPressed()
                }
            },
        )
    }, content = {
        Column {
            Spacer(modifier = Modifier.height(it.calculateTopPadding()))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround,
            ) {
                item {
                    Column(
                        modifier = Modifier.pointerInput(Unit) {
                            detectTapGestures(onPress = {
                                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                pressAMP = 0f
                                tryAwaitRelease()
                                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                view.playSoundEffect(SoundEffectConstants.CLICK)
                                pressAMP = 16f
                            }, onTap = {
                                if (System.currentTimeMillis() - clickTime > 2000) {
                                    clickTime = System.currentTimeMillis()
                                    // TODO to show repository
                                    openUrl(releases)
                                } else {
                                    clickTime = System.currentTimeMillis()
                                }
                            })
                        },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(240.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = CurlyCornerShape(amp = animatedPress.toDouble()),
                                )
                                .shadow(
                                    elevation = 10.dp,
                                    shape = CurlyCornerShape(amp = animatedPress.toDouble()),
                                    ambientColor = MaterialTheme.colorScheme.primaryContainer,
                                    spotColor = MaterialTheme.colorScheme.primaryContainer,
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Image(
                                modifier = Modifier.size(90.dp),
                                painter = painterResource(R.drawable.ic_audio),
                                contentDescription = stringResource(R.string.app_name),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                            )
                        }
                        Spacer(modifier = Modifier.height(48.dp))
                        BadgedBox(badge = {
                            Badge(
                                modifier = Modifier.animateContentSize(tween(800)),
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                contentColor = MaterialTheme.colorScheme.tertiary,
                            ) {
                                Text("v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
                            }
                        }) {
                            Text(
                                text = stringResource(R.string.app_name),
                                style = MaterialTheme.typography.displaySmall
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(48.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        // GitHub
                        RoundIconButton(RoundIconButtonType.GitHub(
                            backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                        ) {
                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            openUrl(github)
                        })
                        Spacer(modifier = Modifier.width(16.dp))

                        // Telegram
                        RoundIconButton(RoundIconButtonType.Telegram(
                            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                        ) {
                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            openUrl(telegram)
                        })
                        Spacer(modifier = Modifier.width(16.dp))

                        // Help
                        RoundIconButton(RoundIconButtonType.Help(
                            backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                        ) {
                            navController.navigate(Route.CREDITS) { launchSingleTop = true }
                        })
                    }
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }
    })


}

@Immutable
sealed class RoundIconButtonType(
    val iconResource: Int? = null,
    val iconVector: ImageVector? = null,
    val descResource: Int? = null,
    val descString: String? = null,
    open val size: Dp = 24.dp,
    open val offset: Modifier = Modifier.offset(),
    open val backgroundColor: Color = Color.Unspecified,
    open val onClick: () -> Unit = {},
) {

    @Immutable
    data class GitHub(
        val desc: String = "GitHub",
        override val backgroundColor: Color,
        override val onClick: () -> Unit = {},
    ) : RoundIconButtonType(
        iconResource = R.drawable.ic_github,
        descString = desc,
        backgroundColor = backgroundColor,
        onClick = onClick,
    )

    @Immutable
    data class Telegram(
        val desc: String = "Telegram",
        override val offset: Modifier = Modifier.offset(x = (-1).dp),
        override val backgroundColor: Color,
        override val onClick: () -> Unit = {},
    ) : RoundIconButtonType(
        iconResource = R.drawable.ic_telegram,
        descString = desc,
        backgroundColor = backgroundColor,
        onClick = onClick,
    )

    @Immutable
    data class Help(
        val desc: Int = R.string.credits,
        override val offset: Modifier = Modifier.offset(x = (3).dp),
        override val backgroundColor: Color,
        override val onClick: () -> Unit = {},
    ) : RoundIconButtonType(
        iconVector = Icons.Rounded.TipsAndUpdates,
        descResource = desc,
        backgroundColor = backgroundColor,
        onClick = onClick,
    )
}

@Composable
private fun RoundIconButton(type: RoundIconButtonType) {
    IconButton(modifier = Modifier
        .size(70.dp)
        .background(
            color = type.backgroundColor,
            shape = CircleShape,
        ), onClick = { type.onClick() }) {
        when (type) {
            is RoundIconButtonType.Help -> {
                Icon(
                    modifier = type.offset.size(type.size),
                    imageVector = type.iconVector!!,
                    contentDescription = stringResource(type.descResource!!),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }

            is RoundIconButtonType.GitHub, is RoundIconButtonType.Telegram -> {
                Icon(
                    modifier = type.offset.size(type.size),
                    painter = painterResource(type.iconResource!!),
                    contentDescription = type.descString,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}