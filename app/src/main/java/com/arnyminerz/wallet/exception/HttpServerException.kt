package com.arnyminerz.wallet.exception

import okhttp3.ResponseBody

class HttpServerException(message: String, statusCode: Int, body: ResponseBody?): HttpResponseException(message, statusCode, body)
