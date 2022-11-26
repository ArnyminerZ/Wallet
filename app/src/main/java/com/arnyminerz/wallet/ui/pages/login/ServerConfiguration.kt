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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arnyminerz.wallet.R
import com.arnyminerz.wallet.account.AccountHelper
import com.arnyminerz.wallet.storage.tempClientId
import com.arnyminerz.wallet.storage.tempClientSecret
import com.arnyminerz.wallet.storage.tempServer
import com.arnyminerz.wallet.utils.doAsync
import com.arnyminerz.wallet.utils.ui
import timber.log.Timber

@ExperimentalMaterial3Api
@Composable
fun ServerConfigurationPage() {
    val context = LocalContext.current

    var fieldsEnabled by remember { mutableStateOf(true) }

    var server by remember { mutableStateOf("https://firefly.arnyminerz.com") }
    var clientId by remember { mutableStateOf("12") }
    var clientSecret by remember { mutableStateOf("XaozykURC7MqjyuYwZ39iecZ1PbGhkhzlPnet54d") }

    Column {
        Text(
            text = stringResource(R.string.title_server_configuration),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            style = MaterialTheme.typography.titleLarge,
        )

        OutlinedTextField(
            value = server,
            onValueChange = { server = it },
            label = { Text(stringResource(R.string.login_server)) },
            supportingText = { Text("https://...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            enabled = fieldsEnabled,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
        )
        OutlinedTextField(
            value = clientId,
            onValueChange = { clientId = it },
            label = { Text(stringResource(R.string.login_oauth_client_id)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            enabled = fieldsEnabled,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        )
        OutlinedTextField(
            value = clientSecret,
            onValueChange = { clientSecret = it },
            label = { Text(stringResource(R.string.login_oauth_client_secret)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            enabled = fieldsEnabled,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        )

        Button(
            enabled = fieldsEnabled,
            onClick = {
                fieldsEnabled = false
                doAsync {
                    Timber.i("Trying to log in...")
                    val ah = AccountHelper.getInstance(context)
                    ah.authoriseClient(context, server, clientId, clientSecret)
                    // val t = ah.login(username, password, server, clientId, clientSecret)
                    // Timber.i("Token: $t")

                    ui { fieldsEnabled = true }
                }

                /*Timber.i("Adding account...")
                val ah = AccountHelper.getInstance(context)
                val added = ah.addAccount(username, password, clientId, clientSecret, server)
                Timber.i("Account added: $added")*/
            },
        ) {
            Text(stringResource(R.string.login_action))
        }
    }
}
