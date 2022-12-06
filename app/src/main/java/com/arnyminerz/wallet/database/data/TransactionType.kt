package com.arnyminerz.wallet.database.data

enum class TransactionType(val type: String) {
    WITHDRAWAL("withdrawal"), DEPOSIT("deposit"), TRANSFER("transfer"), RECONCILIATION("reconciliation"), OPENING_BALANCE("opening balance");

    companion object {
        fun parse(type: String) = values().find { it.type == type }
    }

    override fun toString(): String = type
}