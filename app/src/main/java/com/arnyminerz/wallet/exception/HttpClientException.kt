package com.arnyminerz.wallet.exception

class HttpClientException(message: String, statusCode: Int, body: String?): HttpResponseException(message, statusCode, body)
