package com.arnyminerz.wallet.utils

/**
 * Throws [throwable] only if [this] is false.
 * @author Arnau Mora
 * @since 20220530
 * @return [this]
 */
fun Boolean.drop(throwable: Throwable): Boolean =
    this.also { if (!this) throw throwable }

/**
 * Maps the values of the collection, dropping the ones that throw an error.
 * @author Arnau Mora
 * @since 20221126
 */
fun <T, R> Iterable<T>.safeMap(predicate: (item: T) -> R) =
    mapNotNull {
        try {
            predicate(it)
        } catch (e: Exception) { null }
    }
