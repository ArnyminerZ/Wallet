package com.arnyminerz.wallet

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.LocalActivity
import androidx.compose.material.icons.outlined.Unarchive
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Archive
import androidx.compose.material.icons.rounded.Cloud
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.LocalActivity
import androidx.compose.material.icons.rounded.Unarchive
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arnyminerz.wallet.model.MainViewModel
import com.arnyminerz.wallet.ui.elements.PassViewer
import com.arnyminerz.wallet.ui.theme.WalletTheme
import com.arnyminerz.wallet.ui.view.PassesViewer
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()

    private val picker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri == null) return@registerForActivityResult
            mainViewModel.loadPkPass(uri)
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
                                    onClick = { scope.launch { pagerState.animateScrollToPage(0) } },
                                ) {
                                    Icon(
                                        imageVector = if (pagerState.currentPage == 0)
                                            Icons.Rounded.LocalActivity
                                        else
                                            Icons.Outlined.LocalActivity,
                                        contentDescription = stringResource(R.string.appbar_list)
                                    )
                                }
                                IconButton(
                                    onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                                ) {
                                    Icon(
                                        imageVector = if (pagerState.currentPage == 1)
                                            Icons.Rounded.Inventory2
                                        else
                                            Icons.Outlined.Inventory2,
                                        contentDescription = stringResource(R.string.appbar_archive)
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
                            }
                        )
                    }
                ) { paddingValues ->
                    HorizontalPager(
                        count = 2,
                        state = pagerState,
                    ) { page ->
                        val passes by mainViewModel.passes.observeAsState(emptyList())
                        PassesViewer(
                            passes = passes,
                            filterArchived = page == 1,
                            paddingValues = paddingValues,
                            onArchive = { mainViewModel.archive(it, !it.archived) },
                            onDelete = { mainViewModel.delete(it) },
                        )
                    }
                }
            }
        }
    }
}
