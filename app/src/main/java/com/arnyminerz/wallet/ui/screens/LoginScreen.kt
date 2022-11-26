package com.arnyminerz.wallet.ui.screens

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.launch
import androidx.annotation.IntDef
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.arnyminerz.wallet.R
import com.arnyminerz.wallet.scanner.QrCodeAnalyzer
import com.arnyminerz.wallet.scanner.ScanCode
import com.arnyminerz.wallet.scanner.ScannerActivity
import com.arnyminerz.wallet.storage.authCodes
import com.arnyminerz.wallet.ui.pages.login.ServerConfigurationPage
import com.arnyminerz.wallet.ui.pages.login.SignInPage
import com.arnyminerz.wallet.utils.activity
import com.arnyminerz.wallet.utils.getPreference
import com.arnyminerz.wallet.utils.toast
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

@Preview(
    showSystemUi = true,
    showBackground = true,
)
@Composable
@ExperimentalPagerApi
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
fun LoginScreen() {
    val context = LocalContext.current

    val scannerLauncher = rememberLauncherForActivityResult(ScanCode()) { barcode ->
        if (barcode == null) return@rememberLauncherForActivityResult
        try {
            val json = JSONObject(barcode)
            if (!json.has("server") || !json.has("client_id") || !json.has("client_secret"))
                context.toast(R.string.error_invalid_code)
            else {

            }
        } catch (e: JSONException) {
            context.toast(R.string.error_invalid_code)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { context.activity?.finish() }) {
                        Icon(Icons.Rounded.Close, stringResource(R.string.image_desc_close))
                    }
                },
                title = { Text(text = stringResource(R.string.title_add_account)) },
                actions = {
                    IconButton(
                        onClick = { scannerLauncher.launch() }
                    ) { Icon(Icons.Rounded.QrCodeScanner, stringResource(R.string.image_desc_scan_qr)) }
                },
            )
        }
    ) { paddingValues ->
        ServerConfigurationPage(modifier = Modifier.padding(paddingValues))
    }
}
