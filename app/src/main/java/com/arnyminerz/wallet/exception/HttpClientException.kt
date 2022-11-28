package com.arnyminerz.wallet.exception

import okhttp3.ResponseBody

class HttpClientException(message: String, statusCode: Int, body: ResponseBody?): HttpResponseException(message, statusCode, body)
