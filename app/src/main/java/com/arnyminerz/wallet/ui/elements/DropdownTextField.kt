package com.arnyminerz.wallet.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.PopupProperties
import com.arnyminerz.wallet.ui.utils.keyboardAsState
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber

@Composable
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
fun DropdownTextField(
    value: String,
    onValueChange: (value: String) -> Unit,
    autocomplete: List<String>,
    modifier: Modifier = Modifier,
    label: String? = null,
    onExpandedChanged: (expanded: Boolean) -> Unit = {},
) {
    Box(modifier) {
        var expanded by remember { mutableStateOf(false) }
        val keyboardState by keyboardAsState()

        val skc = LocalSoftwareKeyboardController.current

        LaunchedEffect(keyboardState) {
            snapshotFlow { keyboardState }
                .distinctUntilChanged()
                .collect {
                    if (!it) expanded = false
                }
        }

        OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                expanded = value.isNotBlank() && value.isNotEmpty()
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    if (!it.isFocused)
                        expanded = false
                    else if (value.isNotBlank() && value.isNotEmpty())
                        expanded = true
                    onExpandedChanged(expanded)
                },
            label = { label?.let { Text(it) } },
        )
        DropdownMenu(
            expanded = expanded,
            properties = PopupProperties(
                focusable = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
            ),
            onDismissRequest = {
                Timber.d("Hiding keyboard")
                expanded = false
                skc?.hide()
            },
        ) {
            autocomplete.forEach { item ->
                if (item.lowercase().contains(value.lowercase()))
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = { onValueChange(item) },
                    )
            }
        }
    }
}

@Preview
@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
fun DropdownTextFieldPreview() {
    var text by remember { mutableStateOf("") }
    DropdownTextField(text, { text = it }, listOf("Example", "Values"))
}
