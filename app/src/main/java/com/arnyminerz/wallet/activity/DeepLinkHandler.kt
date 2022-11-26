package com.arnyminerz.wallet.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arnyminerz.wallet.account.AuthCode
import com.arnyminerz.wallet.storage.authCodes
import com.arnyminerz.wallet.storage.tempClientId
import com.arnyminerz.wallet.storage.tempClientSecret
import com.arnyminerz.wallet.storage.tempServer
import com.arnyminerz.wallet.utils.*
import kotlinx.coroutines.flow.first
import timber.log.Timber

class DeepLinkHandler : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uri = intent?.data
        val code = uri?.getQueryParameter("code")
        if (code != null) doAsync {
            Timber.i("Got auth code: $code. Redirection: $uri")

            val server = getPreference(tempServer).first()
            val clientId = getPreference(tempClientId).first()
            val clientSecret = getPreference(tempClientSecret).first()

            if (server != null && clientId != null && clientSecret != null) {
                Timber.i("Storing auth code to preferences...")
                val authCode = AuthCode(server, clientId, clientSecret, code)
                val index = addToPreference(authCodes, authCode)

                ui {
                    launch(MainActivity::class) {
                        putExtra(MainActivity.EXTRA_ACCOUNT_INDEX, index)
                    }
                }
            } else ui {
                // TODO: Localize
                toast("")
                launch(MainActivity::class)
            }
        } else {
            Timber.i("Received uri: $uri")
            finishAffinity()
        }
    }
}