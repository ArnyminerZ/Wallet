package com.arnyminerz.wallet.ui.pages.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arnyminerz.wallet.R
import com.arnyminerz.wallet.account.AccountHelper
import com.arnyminerz.wallet.account.AuthCode
import com.arnyminerz.wallet.storage.authCodes
import com.arnyminerz.wallet.storage.tempClientId
import com.arnyminerz.wallet.storage.tempClientSecret
import com.arnyminerz.wallet.storage.tempServer
import com.arnyminerz.wallet.ui.elements.VisibilityToggleIconButton
import com.arnyminerz.wallet.utils.doAsync
import com.arnyminerz.wallet.utils.getPreference
import com.arnyminerz.wallet.utils.ui
import timber.log.Timber

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun ServerConfigurationPage(modifier: Modifier) {
    val context = LocalContext.current
    val autofill = LocalAutofill.current

    var fieldsEnabled by remember { mutableStateOf(true) }
    var displaySecret by remember { mutableStateOf(false) }

    val clientIdRequester = FocusRequester()
    val clientSecretRequester = FocusRequester()

    var server by remember { mutableStateOf("https://firefly.arnyminerz.com") }
    var clientId by remember { mutableStateOf("4") }
    var clientSecret by remember { mutableStateOf("4L5fOUbEAyrYTRhuET69SKQ5is7yjVRLPrTylAix") }

    val addServer: () -> Unit = {
        fieldsEnabled = false
        doAsync {
            Timber.i("Authorising server...")
            val ah = AccountHelper.getInstance(context)
            ah.authoriseClient(context, server, clientId, clientSecret)

            ui { fieldsEnabled = true }
        }
    }

    Column(modifier) {
        Text(
            text = stringResource(R.string.title_server_configuration),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            style = MaterialTheme.typography.titleLarge,
        )

        val autofillClientIdNode = AutofillNode(
            autofillTypes = listOf(AutofillType.Username),
            onFill = { clientId = it }
        )
        LocalAutofillTree.current += autofillClientIdNode

        val autofillClientSecretNode = AutofillNode(
            autofillTypes = listOf(AutofillType.Password),
            onFill = { clientSecret = it }
        )
        LocalAutofillTree.current += autofillClientSecretNode

        OutlinedTextField(
            value = server,
            onValueChange = { server = it },
            label = { Text(stringResource(R.string.login_server)) },
            supportingText = { Text("https://...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            enabled = fieldsEnabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions { clientIdRequester.requestFocus() },
        )
        OutlinedTextField(
            value = clientId,
            onValueChange = { clientId = it },
            label = { Text(stringResource(R.string.login_oauth_client_id)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .focusRequester(clientIdRequester)
                .onGloballyPositioned { autofillClientIdNode.boundingBox = it.boundsInWindow() }
                .onFocusChanged { focusState ->
                    if (focusState.isFocused)
                        autofill?.requestAutofillForNode(autofillClientIdNode)
                    else
                        autofill?.cancelAutofillForNode(autofillClientIdNode)
                },
            enabled = fieldsEnabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions { clientSecretRequester.requestFocus() },
        )
        OutlinedTextField(
            value = clientSecret,
            onValueChange = { clientSecret = it },
            label = { Text(stringResource(R.string.login_oauth_client_secret)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .focusRequester(clientSecretRequester)
                .onGloballyPositioned { autofillClientSecretNode.boundingBox = it.boundsInWindow() }
                .onFocusChanged { focusState ->
                    if (focusState.isFocused)
                        autofill?.requestAutofillForNode(autofillClientSecretNode)
                    else
                        autofill?.cancelAutofillForNode(autofillClientSecretNode)
                },
            enabled = fieldsEnabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Go,
            ),
            visualTransformation = if (displaySecret) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = { VisibilityToggleIconButton(displaySecret) { displaySecret = !displaySecret } },
            keyboardActions = KeyboardActions { addServer() },
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            Button(
                enabled = fieldsEnabled,
                onClick = addServer,
            ) { Text(stringResource(R.string.login_authorise)) }
        }
    }
}
