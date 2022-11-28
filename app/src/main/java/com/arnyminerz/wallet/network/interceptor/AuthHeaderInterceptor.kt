package com.arnyminerz.wallet.network.interceptor

import com.arnyminerz.wallet.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthHeaderInterceptor(private val tokenType: String, private val accessToken: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain
        .proceed(
            chain.request()
                .newBuilder()
                .header("Authorization", "$tokenType $accessToken")
                .header("Content-Type", "application/vnd.api+json")
                .header("Accept", "application/json")
                .header("User-Agent", BuildConfig.APPLICATION_ID)
                .build()
        )
}
