package com.arnyminerz.wallet.ui.elements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.arnyminerz.wallet.R

@Composable
fun VisibilityToggleIconButton(visible: Boolean, onChangeRequested: () -> Unit) {
    IconButton(onClick = onChangeRequested) {
        Icon(
            if (visible)
                Icons.Rounded.VisibilityOff
            else
                Icons.Rounded.Visibility,
            stringResource(
                if (visible)
                    R.string.image_desc_display_password
                else
                    R.string.image_desc_hide_password,
            ),
        )
    }
}
