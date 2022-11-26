package com.arnyminerz.wallet.activity

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arnyminerz.wallet.R
import com.arnyminerz.wallet.account.AccountHelper
import com.arnyminerz.wallet.account.AuthCode
import com.arnyminerz.wallet.model.MainViewModel
import com.arnyminerz.wallet.storage.authCodes
import com.arnyminerz.wallet.storage.tempClientId
import com.arnyminerz.wallet.storage.tempClientSecret
import com.arnyminerz.wallet.storage.tempServer
import com.arnyminerz.wallet.ui.screens.LoginScreen
import com.arnyminerz.wallet.ui.screens.MainScreen
import com.arnyminerz.wallet.ui.theme.setContentThemed
import com.arnyminerz.wallet.utils.doAsync
import com.arnyminerz.wallet.utils.getPreference
import com.arnyminerz.wallet.utils.popPreference
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.flow.first
import org.json.JSONObject
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ACCOUNT_INDEX = "account_index"
    }

    private lateinit var ah: AccountHelper

    private val mainViewModel by viewModels<MainViewModel>()

    private var accountIndex: Int = -1

    private val picker =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            mainViewModel.loadPkPass(it)
        }

    @OptIn(
        ExperimentalPagerApi::class,
        ExperimentalMaterial3Api::class,
        ExperimentalAnimationApi::class,
        ExperimentalComposeUiApi::class,
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ah = AccountHelper.getInstance(this)

        accountIndex = intent.extras?.getInt(EXTRA_ACCOUNT_INDEX, -1) ?: -1

        setContentThemed {
            val navController = rememberAnimatedNavController()

            BackHandler { finishAffinity() }

            AnimatedNavHost(navController = navController, startDestination = if (accountIndex > 0) "NewAccount" else "Home") {
                composable(
                    "Home",
                    enterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700)) },
                    exitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700)) },
                    popEnterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700)) },
                    popExitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700)) },
                ) { MainScreen(mainViewModel, picker, navController) }
                composable(
                    "AddAccount",
                    enterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700)) },
                    exitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700)) },
                    popEnterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700)) },
                    popExitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700)) },
                ) { LoginScreen() }
                composable(
                    "NewAccount",
                    enterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700)) },
                    exitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700)) },
                    popEnterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700)) },
                    popExitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700)) },
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .align(Alignment.Center),
                        ) {
                            Text(
                                stringResource(R.string.account_adding_title),
                                modifier = Modifier
                                    .fillMaxWidth(),
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                stringResource(R.string.account_adding_message),
                                modifier = Modifier
                                    .fillMaxWidth(),
                                style = MaterialTheme.typography.labelMedium,
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if (accountIndex > 0) doAsync {
            val authCodes = (getPreference(authCodes).first() ?: emptySet()).toList()
            val authCode = JSONObject(authCodes[accountIndex])
                .let { AuthCode.fromJson(it) }
            Timber.i("Registering new account...")
            ah.login(authCode)
            Timber.i("New account added.")
        }
    }

    override fun onResume() {
        super.onResume()

        doAsync {
            popPreference(tempServer)
            popPreference(tempClientId)
            popPreference(tempClientSecret)
        }
    }
}
