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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.arnyminerz.wallet.R
import com.arnyminerz.wallet.account.AccountHelper
import com.arnyminerz.wallet.account.AuthCode
import com.arnyminerz.wallet.storage.authCodes
import com.arnyminerz.wallet.ui.elements.OutlinedDropdownField
import com.arnyminerz.wallet.ui.elements.VisibilityToggleIconButton
import com.arnyminerz.wallet.utils.doAsync
import com.arnyminerz.wallet.utils.getPreference
import com.arnyminerz.wallet.utils.ui
import timber.log.Timber

@Composable
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
fun SignInPage(onNewServerAdditionRequested: () -> Unit) {
    val context = LocalContext.current
    val autofill = LocalAutofill.current

    Column {
        var fieldsEnabled by remember { mutableStateOf(true) }
        var authCodeIndex by remember { mutableStateOf(0) }
        val authCodes: List<AuthCode> by context.getPreference(authCodes, emptySet(), AuthCode.Companion).collectAsState(initial = emptyList())
        var email by remember { mutableStateOf("arnyminer.z@gmail.com") }
        var password by remember { mutableStateOf("2Qmhxw8h2TE8Gbwhc@6rzcK%wZ&w9Q2c") }
        var displayPassword by remember { mutableStateOf(false) }

        val passwordFocusRequester = FocusRequester()

        val autofillEmailNode = AutofillNode(
            autofillTypes = listOf(AutofillType.EmailAddress),
            onFill = { email = it }
        )
        LocalAutofillTree.current += autofillEmailNode

        val autofillPasswordNode = AutofillNode(
            autofillTypes = listOf(AutofillType.Password),
            onFill = { password = it }
        )
        LocalAutofillTree.current += autofillPasswordNode

        val signIn: () -> Unit = {
            fieldsEnabled = false
            doAsync {
                val ah = AccountHelper.getInstance(context)
                val authCode = authCodes[authCodeIndex]
                try {
                    val loginResult = ah.login(authCode)
                    Timber.i("Login: $loginResult")
                } catch (e: IllegalStateException) {
                    Timber.w("Authorization code has expired. Invalidating...")
                    ah.invalidateCode(context, authCode)
                } finally {
                    ui { fieldsEnabled = true }
                }
            }
        }

        OutlinedDropdownField(
            value = authCodeIndex,
            onValueChange = {
                if (it >= 0)
                    authCodeIndex = it
                else
                    onNewServerAdditionRequested()
            },
            enabled = fieldsEnabled,
            values = authCodes
                .mapIndexed { index, authCode -> index to (authCode.host ?: authCode.server) }
                .toMap()
                .let { it.toMutableMap().apply { put(-1, stringResource(R.string.login_server_new)) } },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            placeholder = stringResource(R.string.login_server),
            label = stringResource(R.string.login_server),
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            enabled = fieldsEnabled,
            label = { Text(stringResource(R.string.login_email)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .onGloballyPositioned { autofillEmailNode.boundingBox = it.boundsInWindow() }
                .onFocusChanged { focusState ->
                    if (focusState.isFocused)
                        autofill?.requestAutofillForNode(autofillEmailNode)
                    else
                        autofill?.cancelAutofillForNode(autofillEmailNode)
                },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
            singleLine = true,
            keyboardActions = KeyboardActions { passwordFocusRequester.requestFocus() },
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            enabled = fieldsEnabled,
            label = { Text(stringResource(R.string.login_password)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .focusRequester(passwordFocusRequester)
                .onGloballyPositioned { autofillPasswordNode.boundingBox = it.boundsInWindow() }
                .onFocusChanged { focusState ->
                    if (focusState.isFocused)
                        autofill?.requestAutofillForNode(autofillPasswordNode)
                    else
                        autofill?.cancelAutofillForNode(autofillPasswordNode)
                },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Go,
            ),
            visualTransformation = if (displayPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = { VisibilityToggleIconButton(displayPassword) { displayPassword = !displayPassword } },
            keyboardActions = KeyboardActions { signIn() },
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            Button(
                onClick = signIn,
                enabled = fieldsEnabled,
            ) { Text(stringResource(R.string.login_action)) }
        }
    }
}
