package com.afi.capturewave.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.afi.capturewave.R
import com.afi.capturewave.ui.theme.applyOpacity
import com.afi.capturewave.ui.theme.preferenceTitle

private const val horizontal = 8
private const val vertical = 16

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PreferenceItem(
    title: String,
    description: String? = null,
    icon: Any? = null,
    enabled: Boolean = true,
    onLongClickLabel: String? = null,
    onLongClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    onClick: () -> Unit = {},
) {
    Surface(
        modifier = Modifier.combinedClickable(
            onClick = onClick,
            onClickLabel = onClickLabel,
            enabled = enabled,
            onLongClickLabel = onLongClickLabel,
            onLongClick = onLongClick
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal.dp, vertical.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingIcon?.invoke()

            when (icon) {
                is ImageVector -> {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 16.dp)
                            .size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.applyOpacity(enabled)
                    )
                }

                is Painter -> {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 16.dp)
                            .size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.applyOpacity(enabled)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = if (icon == null && leadingIcon == null) 12.dp else 0.dp)
                    .padding(end = 8.dp)
            ) {
                PreferenceItemTitle(text = title, enabled = enabled)
                if (!description.isNullOrEmpty()) PreferenceItemDescription(
                    text = description, enabled = enabled
                )
            }
            trailingIcon?.let {
                VerticalDivider(
                    modifier = Modifier
                        .height(32.dp)
                        .padding(horizontal = 8.dp)
                        .align(Alignment.CenterVertically),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    thickness = 1.dp
                )
                trailingIcon.invoke()
            }
        }
    }

}


@Composable
fun PreferenceSingleChoiceItem(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    contentPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 18.dp),
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.selectable(
            selected = selected, onClick = onClick
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp)
            ) {
                Text(
                    text = text,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis
                )
            }
            RadioButton(
                selected = selected,
                onClick = onClick,
                modifier = Modifier
                    .padding()
                    .clearAndSetSemantics { },
            )
        }
    }
}

@Composable
internal fun PreferenceItemTitle(
    modifier: Modifier = Modifier,
    text: String,
    maxLines: Int = 2,
    style: TextStyle = preferenceTitle,
    enabled: Boolean,
    color: Color = MaterialTheme.colorScheme.onBackground,
    overflow: TextOverflow = TextOverflow.Ellipsis
) {
    Text(
        modifier = modifier,
        text = text,
        maxLines = maxLines,
        style = style,
        color = color.applyOpacity(enabled),
        overflow = overflow
    )
}

@Composable
internal fun PreferenceItemDescription(
    modifier: Modifier = Modifier,
    text: String,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    enabled: Boolean,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    overflow: TextOverflow = TextOverflow.Ellipsis
) {
    Text(
        modifier = modifier.padding(top = 2.dp),
        text = text,
        maxLines = maxLines,
        style = style,
        color = color.applyOpacity(enabled),
        overflow = overflow
    )
}


@Composable
fun PreferenceSwitch(
    title: String,
    description: String? = null,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    isChecked: Boolean = true,
    checkedIcon: ImageVector = Icons.Outlined.Check,
    onClick: (() -> Unit) = {},
) {
    val thumbContent: (@Composable () -> Unit)? = if (isChecked) {
        {
            Icon(
                imageVector = checkedIcon,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize),
            )
        }
    } else {
        null
    }
    Surface(
        modifier = Modifier.toggleable(
            value = isChecked,
            enabled = enabled,
            onValueChange = { onClick() })
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal.dp, vertical.dp)
                .padding(start = if (icon == null) 12.dp else 0.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            icon?.let {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 16.dp)
                        .size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.applyOpacity(enabled)
                )
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                PreferenceItemTitle(
                    text = title, enabled = enabled
                )
                if (!description.isNullOrEmpty()) PreferenceItemDescription(
                    text = description, enabled = enabled
                )
            }
            Switch(
                checked = isChecked,
                onCheckedChange = null,
                modifier = Modifier.padding(start = 20.dp, end = 6.dp),
                enabled = enabled,
                thumbContent = thumbContent
            )
        }
    }
}


@Composable
fun CreditItem(
    title: String,
    license: String? = null,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    Surface(modifier = Modifier.clickable { onClick() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp)
            ) {
                with(MaterialTheme) {
                    Text(
                        text = title,
                        maxLines = 1,
                        style = typography.titleMedium,
                        color = colorScheme.onSurface.applyOpacity(enabled)
                    )
                    license?.let {
                        Text(
                            text = it,
                            color = colorScheme.onSurfaceVariant.applyOpacity(enabled),
                            maxLines = 2, overflow = TextOverflow.Ellipsis,
                            style = typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun TemplateItem(
    label: String = "",
    template: String? = null,
    selected: Boolean = false,
    isMultiSelectEnabled: Boolean = false,
    checked: Boolean = false,
    onClick: () -> Unit = {},
    onSelect: () -> Unit = {},
    onCheckedChange: (Boolean) -> Unit = {},
    onLongClick: () -> Unit = {}
) {
    Surface(modifier = Modifier.run {
        if (!isMultiSelectEnabled) then(
            combinedClickable(
                onClick = onClick,
                onClickLabel = stringResource(R.string.edit),
                onLongClick = onLongClick,
                onLongClickLabel = stringResource(R.string.multiselect_mode)
            )
        ) else {
            then(toggleable(value = checked, onValueChange = onCheckedChange))
        }
    }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedVisibility(visible = isMultiSelectEnabled) {
                Checkbox(
                    modifier = Modifier.clearAndSetSemantics { },
                    checked = checked,
                    onCheckedChange = onCheckedChange
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp)
            ) {
                with(MaterialTheme) {
                    Text(
                        text = label,
                        maxLines = 1,
                        style = typography.titleMedium,
                        color = colorScheme.onSurface
                    )
                    template?.let {
                        Text(
                            text = it,
                            color = colorScheme.onSurfaceVariant,
                            maxLines = 2, overflow = TextOverflow.Ellipsis,
                            style = typography.bodyMedium,
                        )
                    }
                }
            }


            AnimatedVisibility(!isMultiSelectEnabled) {
                Row {
                    VerticalDivider(
                        modifier = Modifier
                            .height(32.dp)
                            .padding(horizontal = 12.dp)
                            .align(Alignment.CenterVertically),
                        color = MaterialTheme.colorScheme.outlineVariant,
                        thickness = 1.dp
                    )
                    RadioButton(
                        modifier = Modifier.semantics { contentDescription = label },
                        selected = selected,
                        onClick = onSelect
                    )
                }

            }


        }


    }

}