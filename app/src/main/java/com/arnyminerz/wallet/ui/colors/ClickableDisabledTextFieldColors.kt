package com.arnyminerz.wallet.ui.colors

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable

@Composable
@ExperimentalMaterial3Api
fun clickableDisabledTextFieldColors(expanded: Boolean): TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
    disabledBorderColor = if (expanded)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.onSurface.copy(alpha = .7f),
    disabledLabelColor = MaterialTheme.colorScheme.onSurface,
    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurface,
    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .5f),
    disabledSupportingTextColor = MaterialTheme.colorScheme.onBackground,
    disabledTextColor = MaterialTheme.colorScheme.onSurface,
    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface,
)
