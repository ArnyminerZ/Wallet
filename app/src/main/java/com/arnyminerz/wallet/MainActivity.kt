package com.arnyminerz.wallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
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
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arnyminerz.wallet.model.MainViewModel
import com.arnyminerz.wallet.ui.elements.PassViewer
import com.arnyminerz.wallet.ui.screens.LoginScreen
import com.arnyminerz.wallet.ui.screens.MainScreen
import com.arnyminerz.wallet.ui.theme.WalletTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
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
        ExperimentalAnimationApi::class,
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WalletTheme {
                val navController = rememberAnimatedNavController()

                AnimatedNavHost(navController = navController, startDestination = "Home") {
                    composable(
                        "Home",
                        enterTransition = {
                            when (initialState.destination.route) {
                                "Red" ->
                                    slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                                else -> null
                            }
                        },
                        exitTransition = {
                            when (targetState.destination.route) {
                                "Red" ->
                                    slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                                else -> null
                            }
                        },
                        popEnterTransition = {
                            when (initialState.destination.route) {
                                "Red" ->
                                    slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                                else -> null
                            }
                        },
                        popExitTransition = {
                            when (targetState.destination.route) {
                                "Red" ->
                                    slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                                else -> null
                            }
                        }
                    ) { MainScreen(mainViewModel, picker, navController) }
                    composable(
                        "AddAccount",
                        enterTransition = {
                            when (initialState.destination.route) {
                                "Red" ->
                                    slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                                else -> null
                            }
                        },
                        exitTransition = {
                            when (targetState.destination.route) {
                                "Red" ->
                                    slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                                else -> null
                            }
                        },
                        popEnterTransition = {
                            when (initialState.destination.route) {
                                "Red" ->
                                    slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                                else -> null
                            }
                        },
                        popExitTransition = {
                            when (targetState.destination.route) {
                                "Red" ->
                                    slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                                else -> null
                            }
                        }
                    ) { LoginScreen() }
                }
            }
        }
    }
}
