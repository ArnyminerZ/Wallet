package com.arnyminerz.wallet.database.data

enum class FireflyTargetType(val type: String) {
    DefaultAccount("Default account"),
    CashAccount("Cash account"),
    AssetAccount("Asset account"),
    ExpenseAccount("Expense account"),
    RevenueAccount("Revenue account"),
    InitialBalanceAccount("Initial balance account"),
    BeneficiaryAccount("Beneficiary account"),
    ImportAccount("Import account"),
    ReconciliationAccount("Reconciliation account"),
    Loan("Loan"),
    Debt("Debt"),
    Mortgage("Mortgage");

    companion object {
        fun parse(type: String) = values().find { it.type == type }
    }

    override fun toString(): String = type
}