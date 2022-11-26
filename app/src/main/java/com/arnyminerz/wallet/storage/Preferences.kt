package com.arnyminerz.wallet.storage

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

val authCodes = stringSetPreferencesKey("auth_codes")

val tempServer = stringPreferencesKey("temp_server")
val tempClientId = stringPreferencesKey("temp_client_id")
val tempClientSecret = stringPreferencesKey("temp_client_key")
