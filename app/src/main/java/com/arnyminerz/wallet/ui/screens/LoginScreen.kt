package com.arnyminerz.wallet.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arnyminerz.wallet.R
import com.arnyminerz.wallet.account.AccountHelper
import com.arnyminerz.wallet.utils.doAsync
import com.arnyminerz.wallet.utils.ui
import timber.log.Timber

@Preview(
    showSystemUi = true,
    showBackground = true,
)
@Composable
@ExperimentalMaterial3Api
fun LoginScreen() {
    val context = LocalContext.current

    Column {
        var fieldsEnabled by remember { mutableStateOf(true) }

        var username by remember { mutableStateOf("arnyminer.z@gmail.com") }
        var password by remember { mutableStateOf("2Qmhxw8h2TE8Gbwhc@6rzcK%wZ&w9Q2c") }
        var server by remember { mutableStateOf("https://firefly.arnyminerz.com") }
        var clientId by remember { mutableStateOf("2") }
        var clientSecret by remember { mutableStateOf("-") }

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(stringResource(R.string.login_username)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            enabled = fieldsEnabled,
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.login_password)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            enabled = fieldsEnabled,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                    val t = ah.login(username, password, server, clientId, clientSecret)
                    Timber.i("Token: $t")

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
