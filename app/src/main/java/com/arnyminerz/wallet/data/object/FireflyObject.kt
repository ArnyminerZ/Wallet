package com.arnyminerz.wallet.data.`object`

import com.arnyminerz.wallet.utils.serializer.JsonSerializable

abstract class FireflyObject(
    open val id: Long,
): JsonSerializable()
