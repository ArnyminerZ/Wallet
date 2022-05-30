package com.arnyminerz.wallet.utils

/**
 * Throws [throwable] only if [this] is false.
 * @author Arnau Mora
 * @since 20220530
 * @return [this]
 */
fun Boolean.drop(throwable: Throwable): Boolean =
    this.also { if (!this) throw throwable }
