package com.arnyminerz.wallet.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.view.WindowCompat
import com.arnyminerz.wallet.ui.screens.LoginScreen
import com.arnyminerz.wallet.ui.theme.setContentThemed
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalPagerApi::class, ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
class AddAccountActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentThemed {
            LoginScreen()
        }
    }
}