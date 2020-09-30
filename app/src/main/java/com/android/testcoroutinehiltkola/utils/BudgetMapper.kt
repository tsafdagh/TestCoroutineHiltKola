package com.android.testcoroutinehiltkola.utils

import com.android.testcoroutinehiltkola.enums.RANGBUDGET
import com.android.testcoroutinehiltkola.model.Budget
import com.android.testcoroutinehiltkola.model.BudgetTable
import java.util.*
import javax.inject.Inject

class BudgetMapper @Inject constructor() : EntityMapper<BudgetTable, Budget> {
    override fun mapFromEntity(budget: Budget): BudgetTable {
        return BudgetTable(
            budget.id,
            budget.name,
            budget.rang.name,
            budget.budgetAmount,
            budget.startedDate.time,
            budget.createdDate.time,
            budget.reccurence,
            budget.notification,
            budget.active
        )
    }

    override fun mapToEntity(budgetTable: BudgetTable): Budget {
        return Budget(
            budgetTable.id,
            budgetTable.name,
            RANGBUDGET.valueOf(budgetTable.rang),
            budgetTable.budgetAmount,
            Date(budgetTable.startedDate),
            Date(budgetTable.createdDate),
            budgetTable.reccurence,
            budgetTable.notification,
            budgetTable.active,
            mutableListOf(),
            mutableListOf()
        )
    }

    fun mapListToEntity(list: List<BudgetTable>): List<Budget>{
        return list.map { mapToEntity(it) }
    }

}