package com.android.testcoroutinehiltkola.repository

import NetworkBoundResource
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.android.testcoroutinehiltkola.di.Local
import com.android.testcoroutinehiltkola.di.Remote
import com.android.testcoroutinehiltkola.model.Budget
import com.android.testcoroutinehiltkola.remote.RemoteDataSource
import com.android.testcoroutinehiltkola.room.LocalDataSource
import com.android.testcoroutinehiltkola.utils.AppExecutors
import com.android.testcoroutinehiltkola.utils.FirebaseResponseType
import com.android.testcoroutinehiltkola.utils.Resource
import com.android.testcoroutinehiltkola.utils.printlnApp
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class Repository
@Inject constructor(
    @Local val localDataSource: DataSource,
    @Remote val remoteDataSource: DataSource,
    val appExecutors: AppExecutors
) {

    private val TAG = "Repository"

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

    fun updateBudget(budget: Budget) {
        printlnApp("update budget...")
        GlobalScope.launch {
            localDataSource.updateBudget(budget)
            remoteDataSource.updateBudget(budget)
        }
    }

    fun getCachedBudgets(): Flow<Resource<List<Budget>>> = flow {
        printlnApp("get budgets...")

        // get data from local
        val budgetsFromLocalFlow = (localDataSource as LocalDataSource).getCachedBudgets()

        emit(Resource.Success(budgetsFromLocalFlow))
    }

    fun getSynckBudgetRealTime(): Flow<Resource<List<Budget>>> = flow {

        try {
            //get data from remote
            val budgetFromRemoteFlow = remoteDataSource.getBudgets()
            budgetFromRemoteFlow.collect { remoteBudgetList ->
                printlnApp("get budgets from remote $remoteBudgetList")
                //insert all remote data in caches
                printlnApp("insert all remote data in caches")
                remoteBudgetList.forEach {
                    localDataSource.createBudget(it).onEach { }.launchIn(GlobalScope)
                }
                //emit remote data
                emit(Resource.Success(remoteBudgetList))
            }

        } catch (e: Exception) {
            printlnApp("Emit error: emit exception")
            emit(Resource.Failure(e, null))
        }
    }

    @InternalCoroutinesApi
    fun getBudget2(): Flow<Resource<List<Budget>>> {
        return NetworkBoundResource(
            fetchFromLocal = {
                localDataSource.getBudgets()
            },
            shouldFetchFromRemote = { budgetList ->
                ((budgetList?.isEmpty()) ?: true) //now we w'll refresh only if cahed data is empty
            },
            fetchFromRemote = {
                    (remoteDataSource as RemoteDataSource).getBudgetsFirebaseRequest()
            },
            processRemoteResponse = {
                val sucessResponse = (it as FirebaseResponseType.FirebaseSuccesResponse)
                sucessResponse.body
            },
            saveRemoteData = { budgetList ->
                budgetList.forEach {
                    localDataSource.createBudget(it).onEach {
                        Log.d(TAG, "getBudget2: new cashflow insert")
                    }.launchIn(GlobalScope)
                }
            },
            onFetchFailed = { errorBody: String?, statusCode: Int ->

            }
        ).flowOn(Dispatchers.IO)
    }
}