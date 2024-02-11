package com.afi.capturewave.ui.pages.settings.about

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.afi.capturewave.R
import com.afi.capturewave.ui.component.BackButton
import com.afi.capturewave.ui.component.CreditItem
import com.afi.capturewave.ui.component.LargeTopAppBar

data class Credit(val title: String = "", val license: String? = null, val url: String = "")

const val GPL_V3 = "GNU General Public License v3.0"
const val APACHE_V2 = "Apache License, Version 2.0"

const val recordYou = "https://github.com/you-apps/RecordYou"
const val readYou = "https://github.com/Ashinch/ReadYou"
const val materialIcon = "https://fonts.google.com/icons"
const val jetpack = "https://github.com/androidx/androidx"
const val kotlin = "https://kotlinlang.org/"
const val material3 = "https://m3.material.io/"
const val seal = "https://github.com/JunkFood02/Seal"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditsPage(onBackPressed: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState(),
        canScroll = { true })

    val creditsList = listOf(
        Credit("Android Jetpack", APACHE_V2, jetpack),
        Credit("Kotlin", APACHE_V2, kotlin),
        Credit("Record You", GPL_V3, recordYou),
        Credit("Read You", GPL_V3, readYou),
        Credit("Material Design 3", APACHE_V2, material3),
        Credit("Material Icons", APACHE_V2, materialIcon),
        Credit("Seal", GPL_V3, seal)
    )

    val uriHandler = LocalUriHandler.current
    fun openUrl(url: String) {
        uriHandler.openUri(url)
    }

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(title = {
                Text(
                    modifier = Modifier,
                    text = stringResource(id = R.string.credits),
                    fontFamily = FontFamily(Font(R.font.en_light))
                )
            }, navigationIcon = {
                BackButton {
                    onBackPressed()
                }
            }, scrollBehavior = scrollBehavior
            )
        },
        content = {
            LazyColumn(modifier = Modifier.padding(it)) {
                items(creditsList) { item ->
                    CreditItem(title = item.title, license = item.license) { openUrl(item.url) }
                }
            }
        }
    )
}