package com.arnyminerz.wallet.exception

class HttpServerException(message: String, statusCode: Int, body: String?): HttpResponseException(message, statusCode, body)
