package com.android.testcoroutinehiltkola.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import com.android.testcoroutinehiltkola.model.Budget
import com.android.testcoroutinehiltkola.repository.DataSource
import com.android.testcoroutinehiltkola.utils.BudgetMapper
import com.android.testcoroutinehiltkola.utils.printlnApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LocalDataSource @Inject constructor(val localMapper: BudgetMapper, val budgetDao: BudgetDao) :
    DataSource {

    override fun createBudget(budget: Budget): Flow<Boolean> = flow {
        val subscription = budgetDao.insert(localMapper.mapFromEntity(budget))
        printlnApp("create budget locally...")
        emit(true)
    }

    override suspend fun updateBudget(budget: Budget): Boolean {
        printlnApp("update budget locally...")
        budgetDao.update(localMapper.mapFromEntity(budget))
        return true
    }

    override fun getBudgets(): Flow<List<Budget>> =
        budgetDao.getAllBudget().map { localMapper.mapListToEntity(it) }

/*    override fun getBudgets(): Flow<List<Budget>> = flow {
        budgetDao.getAllBudget().collect { budgetTable ->
            printlnApp("get budgets locally...")
            emit(localMapper.mapListToEntity(budgetTable))
        }
    }*/


    suspend fun getCachedBudgets(): List<Budget> {
        return budgetDao.getCachedBudgets().map {
            localMapper.mapToEntity(it)
        }
    }

}