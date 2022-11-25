package com.arnyminerz.wallet.scanner

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class ScanCode: ActivityResultContract<Void?, String?>() {
    override fun createIntent(context: Context, input: Void?): Intent = Intent(context, ScannerActivity::class.java)

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        if (resultCode != Activity.RESULT_OK) return null
        return intent?.getStringExtra(ScannerActivity.RESULT_EXTRA_DATA)
    }
}