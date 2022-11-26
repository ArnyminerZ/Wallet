package com.arnyminerz.wallet.ui.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester

@Composable
@ExperimentalMaterial3Api
fun <K: Any> OutlinedDropdownField(
    value: K,
    onValueChange: (value: K) -> Unit,
    values: Map<K, String>,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    label: String? = null,
) {
    Box(modifier = modifier) {
        var expanded by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = values[value] ?: "",
            onValueChange = {},
            readOnly = true,
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            colors = TextFieldDefaults.outlinedTextFieldColors(
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
            ),
            placeholder = { placeholder?.let { Text(it) } },
            label = { label?.let { Text(it) } },
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            values.toList().forEach { (key, value) ->
                DropdownMenuItem(
                    text = { Text(value) },
                    onClick = { onValueChange(key); expanded = false },
                )
            }
        }
    }
}
