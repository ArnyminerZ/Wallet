package com.arnyminerz.wallet.ui.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.arnyminerz.wallet.ui.colors.clickableDisabledTextFieldColors

@Composable
@ExperimentalMaterial3Api
fun <K: Any> OutlinedDropdownField(
    value: K,
    onValueChange: (value: K) -> Unit,
    values: Map<K, String>,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    label: String? = null,
    enabled: Boolean = true,
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
                .clickable(enabled = enabled) { expanded = !expanded },
            colors = clickableDisabledTextFieldColors(expanded),
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
