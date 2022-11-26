package com.arnyminerz.wallet.ui.pages.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.arnyminerz.wallet.R
import com.arnyminerz.wallet.account.AuthCode
import com.arnyminerz.wallet.storage.authCodes
import com.arnyminerz.wallet.ui.elements.OutlinedDropdownField
import com.arnyminerz.wallet.utils.getPreference

@Composable
@ExperimentalMaterial3Api
fun SignInPage() {
    val context = LocalContext.current

    Column {
        var server by remember { mutableStateOf(0) }
        val servers by context.getPreference(authCodes, emptySet(), AuthCode.Companion).collectAsState(initial = emptySet())

        OutlinedDropdownField(
            value = server,
            onValueChange = { server = it },
            values = servers.mapIndexed { index, authCode -> index to (authCode.host ?: authCode.server) }.toMap(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            placeholder = stringResource(R.string.login_server),
            label = stringResource(R.string.login_server),
        )
        OutlinedTextField(
            value = "",
            onValueChange = { },
            label = { Text(stringResource(R.string.login_server)) },
            supportingText = { Text("https://...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
        )
    }
}
