package com.android.testcoroutinehiltkola.repository

import androidx.lifecycle.LiveData
import com.android.testcoroutinehiltkola.di.Local
import com.android.testcoroutinehiltkola.di.Remote
import com.android.testcoroutinehiltkola.model.Budget
import com.android.testcoroutinehiltkola.utils.DataState
import com.android.testcoroutinehiltkola.utils.printlnApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class Repository
@Inject constructor(
    @Local val localDataSource: DataSource,
    @Remote val remoteDataSource: DataSource){

    suspend fun createBudget(budget: Budget) {
        printlnApp("create budget...")
        remoteDataSource.createBudget(budget)
            .collect {
                printlnApp("create budget remotly : $it")
            }
        localDataSource.createBudget(budget)
            .onEach {
                printlnApp("create budget locally : $it")
            }.launchIn(GlobalScope)
    }

    fun updateBudget (budget: Budget){
        printlnApp("update budget...")
        GlobalScope.launch{
            localDataSource.updateBudget(budget)
            remoteDataSource.updateBudget(budget)
        }
    }

    suspend fun getBudgets (): Flow<DataState<List<Budget>>> = flow {
        printlnApp("get budgets...")
        emit(DataState.Loading)
        try{
            val budgets = localDataSource.getBudgets()
            printlnApp("get budgets $budgets")
            emit(DataState.Success(budgets))
        }catch (e: Exception){
            emit(DataState.Error(e))
        }
    }
}