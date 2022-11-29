package com.arnyminerz.wallet.ui.screens

import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Cloud
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.arnyminerz.wallet.R
import com.arnyminerz.wallet.model.MainViewModel
import com.arnyminerz.wallet.ui.elements.PassViewer
import com.arnyminerz.wallet.ui.pages.firefly.FireflyDashboard
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import timber.log.Timber

const val PAGE_PASSES = 0
const val PAGE_MONEY = 1

@Composable
@ExperimentalPagerApi
@ExperimentalMaterial3Api
fun MainScreen(
    mainViewModel: MainViewModel = viewModel(),
    pkPassPicker: ActivityResultLauncher<String>? = null,
    navController: NavController? = null,
    pagerState: PagerState
) {
    val scope = rememberCoroutineScope()
    val accounts by mainViewModel.accounts.observeAsState(emptyArray())

    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(
                        onClick = { scope.launch { pagerState.scrollToPage(PAGE_PASSES) } },
                    ) {
                        Icon(
                            imageVector = if (pagerState.currentPage == PAGE_PASSES)
                                Icons.Rounded.List
                            else
                                Icons.Outlined.List,
                            contentDescription = stringResource(R.string.appbar_list)
                        )
                    }
                    IconButton(
                        onClick = { scope.launch { pagerState.scrollToPage(PAGE_MONEY) } },
                    ) {
                        Icon(
                            imageVector = if (pagerState.currentPage == PAGE_MONEY)
                                Icons.Rounded.Cloud
                            else
                                Icons.Outlined.Cloud,
                            contentDescription = stringResource(R.string.appbar_cloud)
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { pkPassPicker?.launch("application/*") },
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
            when (page) {
                PAGE_PASSES -> if (mainViewModel.pass != null) {
                    Timber.i("Loading pass...")
                    PassViewer(pass = mainViewModel.pass!!)
                } else Timber.i("Bitmap and/or barcode are null.")
                PAGE_MONEY -> Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                ) {
                    Button(
                        onClick = {
                            navController?.navigate("AddAccount")
                        }
                    ) {
                        Text("Add account")
                    }
                    if (accounts.isNotEmpty())
                        FireflyDashboard(mainViewModel, accounts.first())
                }
                else -> Text("Hello world from page $page!")
            }
        }
    }
}
