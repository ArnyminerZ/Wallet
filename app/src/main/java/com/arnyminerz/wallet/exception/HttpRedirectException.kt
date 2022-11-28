package com.arnyminerz.wallet.exception

import okhttp3.ResponseBody

class HttpRedirectException(message: String, statusCode: Int, body: ResponseBody?): HttpResponseException(message, statusCode, body)
