package com.arnyminerz.wallet.ui.screens

import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.arnyminerz.wallet.R
import com.arnyminerz.wallet.activity.MainActivity
import com.arnyminerz.wallet.model.MainViewModel
import com.arnyminerz.wallet.ui.elements.PassViewer
import com.arnyminerz.wallet.ui.pages.firefly.FireflyDashboard
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import timber.log.Timber

const val PAGE_MONEY = 0
const val PAGE_PASSES = 1

private val pages = mapOf(
    PAGE_MONEY to ((Icons.Rounded.Cloud to Icons.Outlined.Cloud) to R.string.appbar_cloud),
    PAGE_PASSES to ((Icons.Rounded.List to Icons.Outlined.List) to R.string.appbar_list),
)

@Preview(
    showSystemUi = true,
    showBackground = true,
)
@Composable
@ExperimentalPagerApi
@ExperimentalMaterial3Api
fun MainScreen(
    mainViewModel: MainViewModel = viewModel(),
    pkPassPicker: ActivityResultLauncher<String>? = null,
    navController: NavController? = null,
    pagerState: PagerState = rememberPagerState(),
) {
    val scope = rememberCoroutineScope()
    val accounts by mainViewModel.accounts.observeAsState(emptyArray())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.welcome)) },
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    for ((index, data) in pages) {
                        val (icons, text) = data
                        val (selectedIcon, unselectedIcon) = icons

                        IconButton(
                            onClick = { scope.launch { pagerState.scrollToPage(index) } },
                        ) {
                            Icon(
                                imageVector = if (pagerState.currentPage == index)
                                    selectedIcon
                                else
                                    unselectedIcon,
                                contentDescription = stringResource(text)
                            )
                        }
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            when (pagerState.currentPage) {
                                0 -> pkPassPicker?.launch("application/*")
                                1 -> navController?.navigate(MainActivity.SCREEN_NEW_TRANSACTION)
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = stringResource(R.string.action_add),
                        )
                    }
                },
            )
        },
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
