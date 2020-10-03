package com.android.testcoroutinehiltkola.remote

import androidx.lifecycle.LiveData
import com.android.testcoroutinehiltkola.model.Budget
import com.android.testcoroutinehiltkola.repository.DataSource
import com.android.testcoroutinehiltkola.utils.FirebaseResponseType
import com.android.testcoroutinehiltkola.utils.printlnApp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@ExperimentalCoroutinesApi
class RemoteDataSource @Inject constructor(val firebaseFirestore: FirebaseFirestore) : DataSource {

    val BUDGET_COLLECTION = "Budget"
    override fun createBudget(budget: Budget): Flow<Boolean> = callbackFlow {
        printlnApp("create budget remotely...")
        var bool = false
        val subscription = firebaseFirestore.collection(BUDGET_COLLECTION)
            .document(budget.id)
            .set(budget)
            .addOnSuccessListener {
                bool = true
                offer(bool)
            }.addOnFailureListener {
                bool = false
                offer(bool)
            }

        awaitClose { subscription.isCanceled }
    }

    override suspend fun updateBudget(budget: Budget): Boolean {
        printlnApp("update budget remotely...")
        var bool = false
        firebaseFirestore.collection(BUDGET_COLLECTION)
            .document(budget.id)
            .set(budget)
            .addOnSuccessListener {
                bool = true
            }.addOnFailureListener {
                bool = false
            }
        return bool
    }

    override fun getBudgets(): Flow<List<Budget>> = flow {
        printlnApp("get budgets remotely...")

        try {
            val data = firebaseFirestore
                .collection("Budget")
                .get()
                .await()

            val budgetList = mutableListOf<Budget>()
            data.forEach { documentSnapChot ->
                val currentBudget = documentSnapChot.toObject(Budget::class.java)
                budgetList.add(currentBudget)
            }
            emit(budgetList)
        } catch (ex: Exception) {
            emit(arrayListOf())
        }

    }

    fun getBudgetsFirebaseRequest(): Flow<FirebaseResponseType<List<Budget>>> = flow {
        printlnApp("get budgets remotely...")

        try {
            val data = firebaseFirestore
                .collection("Budget")
                .get()
                .await()

            val budgetList = mutableListOf<Budget>()
            data.forEach { documentSnapChot ->
                val currentBudget = documentSnapChot.toObject(Budget::class.java)
                budgetList.add(currentBudget)
            }
            if (budgetList.isNotEmpty()) {
                emit(FirebaseResponseType.FirebaseSuccesResponse(budgetList))
            } else {
                emit(FirebaseResponseType.FirebaseEmptyResponse(null))
            }
        } catch (ex: Exception) {
            emit(FirebaseResponseType.FirebaseErrorResponse(ex))
        }

    }

    fun getBudgetsFirebaseRequest2(): Flow<FirebaseResponseType<List<Budget>>> = callbackFlow {
        printlnApp("get budgets remotely...")

        val budgetDocuments = firebaseFirestore
            .collection("Budget")

        val subscription = budgetDocuments.addSnapshotListener { snapshot, error ->

            if (error != null) {
                offer(FirebaseResponseType.FirebaseErrorResponse(error))
            } else {
                when {
                    (snapshot == null) -> {
                        offer(FirebaseResponseType.FirebaseEmptyResponse(null))
                    }
                    (snapshot.isEmpty) -> {
                        offer(FirebaseResponseType.FirebaseEmptyResponse(null))
                    }
                    else -> {
                        val budgetList = mutableListOf<Budget>()
                        snapshot.documents.forEach { documentSnapChot ->
                            val currentBudget = documentSnapChot.toObject(Budget::class.java)
                            budgetList.add(currentBudget!!)
                        }
                        offer(FirebaseResponseType.FirebaseSuccesResponse(budgetList))
                    }
                }
            }
        }
        awaitClose { subscription.remove() }
    }


}