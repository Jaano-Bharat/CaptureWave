package com.afi.capturewave.ui.pages.recordings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.afi.capturewave.R
import com.afi.capturewave.enums.SortOrder
import com.afi.capturewave.ui.component.BackButton
import com.afi.capturewave.ui.component.ClickableIcon
import com.afi.capturewave.ui.component.LargeTopAppBar
import com.afi.capturewave.ui.dialogs.ConfirmationDialog
import com.afi.capturewave.ui.models.PlayerModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    showVideoModeInitially: Boolean, onBackPressed: () -> Unit
) {
    var showDeleteDialog by remember {
        mutableStateOf(false)
    }
    var selectedSortOrder by remember {
        mutableStateOf(SortOrder.ALPHABETIC)
    }
    val playerModel: PlayerModel = viewModel(factory = PlayerModel.Factory)

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    )

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(title = { Text(stringResource(R.string.recordings)) },
                actions = {
                    Box {
                        var showDropDown by remember {
                            mutableStateOf(false)
                        }
                        ClickableIcon(
                            imageVector = Icons.AutoMirrored.Rounded.Sort,
                            contentDescription = stringResource(R.string.sort)
                        ) {
                            showDropDown = true
                        }

                        val sortOptions = listOf(
                            SortOrder.DEFAULT to R.string.default_sort,
                            SortOrder.ALPHABETIC to R.string.alphabetic,
                            SortOrder.ALPHABETIC_REV to R.string.alphabetic_rev,
                            SortOrder.SIZE to R.string.size,
                            SortOrder.SIZE_REV to R.string.size_rev
                        )
                        DropdownMenu(showDropDown, { showDropDown = false }) {
                            sortOptions.forEach { sortOrder ->
                                DropdownMenuItem(text = {
                                    Text(stringResource(sortOrder.second))
                                }, onClick = {
                                    selectedSortOrder = sortOrder.first
                                    playerModel.sortItems(sortOrder.first)
                                    showDropDown = false
                                })
                            }
                        }
                    }
                    if (playerModel.selectedFiles.isNotEmpty()) {
                        val selectedAll =
                            playerModel.selectedFiles.size == playerModel.audioRecordingItems.size + playerModel.screenRecordingItems.size
                        Checkbox(modifier = Modifier.align(Alignment.CenterVertically),
                            checked = selectedAll,
                            onCheckedChange = {
                                if (selectedAll) {
                                    playerModel.selectedFiles = listOf()
                                } else {
                                    playerModel.selectedFiles =
                                        playerModel.screenRecordingItems + playerModel.audioRecordingItems
                                }
                            })
                    }
                    ClickableIcon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = stringResource(R.string.delete_all)
                    ) {
                        showDeleteDialog = true
                    }
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = { BackButton { onBackPressed() } })
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            PlayerView(
                showVideoModeInitially
            )
        }
    }

    if (showDeleteDialog) {
        ConfirmationDialog(title = if (playerModel.selectedFiles.isEmpty()) R.string.delete_all else R.string.delete,
            onDismissRequest = { showDeleteDialog = false }) {
            playerModel.deleteFiles()
        }
    }
}