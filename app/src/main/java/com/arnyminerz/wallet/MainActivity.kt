package com.arnyminerz.wallet

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocalActivity
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.LocalActivity
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arnyminerz.wallet.model.MainViewModel
import com.arnyminerz.wallet.pkpass.data.Pass
import com.arnyminerz.wallet.ui.elements.PassViewer
import com.arnyminerz.wallet.ui.theme.WalletTheme
import com.arnyminerz.wallet.ui.view.PassesViewer
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.distinctUntilChanged
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

        handleIntent()

        setContent {
            WalletTheme {
                val pagerState = rememberPagerState()
                val scope = rememberCoroutineScope()

                val bottomSheetState = rememberModalBottomSheetState(true)

                var showingPass by remember { mutableStateOf<Pass?>(null) }
                if (showingPass != null)
                    ModalBottomSheet(
                        onDismissRequest = { showingPass = null },
                        sheetState = bottomSheetState,
                    ) {
                        showingPass?.let { pass ->
                            PassViewer(
                                pass = pass,
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = MaterialTheme.colorScheme.onSurface,
                                ),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
                            )
                        }
                    }

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
                        modifier = Modifier.padding(paddingValues),
                    ) { page ->
                        val passes by mainViewModel.passes.observeAsState(emptyList())
                        PassesViewer(
                            passes = passes,
                            filterArchived = page == 1,
                            onArchive = { mainViewModel.archive(it, !it.archived) },
                            onDelete = { mainViewModel.delete(it) },
                            onView = { showingPass = it },
                        )
                    }
                }
            }
        }
    }

    private fun handleIntent() {
        if (intent.action != Intent.ACTION_VIEW) return
        if (intent.data == null) return

        Timber.i("Uri: ${intent.data}")
    }
}
