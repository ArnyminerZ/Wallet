package com.arnyminerz.wallet.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arnyminerz.wallet.utils.toast
import timber.log.Timber

class DeepLinkHandler: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uri = intent?.data
        val code = uri?.getQueryParameter("code")
        if (code != null) {
            Timber.i("Got auth code: $code")
            toast("Authorisation complete!")
            // TODO: Go to log in
            finishAffinity()
        } else {
            Timber.i("Received uri: $uri")
            finishAffinity()
        }
    }
}