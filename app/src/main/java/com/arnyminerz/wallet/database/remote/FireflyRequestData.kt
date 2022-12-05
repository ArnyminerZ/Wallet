package com.arnyminerz.wallet.database.remote

data class FireflyRequestData(
    val server: String,
    val authTokenType: String,
    val authToken: String,
)
