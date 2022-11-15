package com.arnyminerz.wallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Cloud
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.arnyminerz.wallet.model.MainViewModel
import com.arnyminerz.wallet.ui.elements.PassViewer
import com.arnyminerz.wallet.ui.screens.LoginScreen
import com.arnyminerz.wallet.ui.theme.WalletTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModels<MainViewModel>()

    private val picker =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            mainViewModel.loadPkPass(
                it
            )
        }

    @OptIn(
        ExperimentalPagerApi::class,
        ExperimentalMaterial3Api::class,
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WalletTheme {
                val pagerState = rememberPagerState()
                val scope = rememberCoroutineScope()

                Scaffold(
                    bottomBar = {
                        BottomAppBar(
                            actions = {
                                IconButton(
                                    onClick = { scope.launch { pagerState.scrollToPage(0) } },
                                ) {
                                    Icon(
                                        imageVector = if (pagerState.currentPage == 0)
                                            Icons.Rounded.List
                                        else
                                            Icons.Outlined.List,
                                        contentDescription = stringResource(R.string.appbar_list)
                                    )
                                }
                                IconButton(
                                    onClick = { scope.launch { pagerState.scrollToPage(1) } },
                                ) {
                                    Icon(
                                        imageVector = if (pagerState.currentPage == 1)
                                            Icons.Rounded.Cloud
                                        else
                                            Icons.Outlined.Cloud,
                                        contentDescription = stringResource(R.string.appbar_cloud)
                                    )
                                }
                            },
                            floatingActionButton = {
                                FloatingActionButton(
                                    onClick = {
                                        picker.launch("application/*")
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Add,
                                        contentDescription = stringResource(R.string.action_add),
                                    )
                                }
                            },
                        )
                    }
                ) { paddingValues ->
                    HorizontalPager(
                        count = 2,
                        state = pagerState,
                        modifier = Modifier.padding(paddingValues),
                    ) { page ->
                        when(page) {
                            0 -> if (mainViewModel.pass != null) {
                                Timber.i("Loading pass...")
                                PassViewer(pass = mainViewModel.pass!!)
                            } else Timber.i("Bitmap and/or barcode are null.")
                            1 -> LoginScreen()
                            else -> Text("Hello world from page $page!")
                        }
                    }
                }
            }
        }
    }
}
