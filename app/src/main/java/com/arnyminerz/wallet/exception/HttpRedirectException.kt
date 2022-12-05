package com.arnyminerz.wallet.exception

class HttpRedirectException(message: String, statusCode: Int, body: String?): HttpResponseException(message, statusCode, body)
