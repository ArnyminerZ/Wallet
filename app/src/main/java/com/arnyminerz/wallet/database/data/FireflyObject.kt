package com.arnyminerz.wallet.database.data

import com.arnyminerz.wallet.utils.serializer.JsonSerializable

abstract class FireflyObject(
    open val id: Long,
): JsonSerializable()
