package com.android.testcoroutinehiltkola.room

import androidx.lifecycle.LiveData
import com.android.testcoroutinehiltkola.model.Budget
import com.android.testcoroutinehiltkola.repository.DataSource
import com.android.testcoroutinehiltkola.utils.BudgetMapper
import com.android.testcoroutinehiltkola.utils.printlnApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LocalDataSource @Inject constructor(val localMapper: BudgetMapper, val budgetDao: BudgetDao) : DataSource {

    override fun createBudget(budget: Budget) : Flow<Boolean> = flow {
        printlnApp("create budget locally...")
        val subscription = budgetDao.insert(localMapper.mapFromEntity(budget))
        emit(true)
    }

    override suspend fun updateBudget(budget: Budget) : Boolean {
        printlnApp("update budget locally...")
        budgetDao.update(localMapper.mapFromEntity(budget))
        return true
    }

    override suspend fun getBudgets(): List<Budget> {
        printlnApp("get budgets locally...")
        return localMapper.mapListToEntity(budgetDao.getAllBudget())
    }
}