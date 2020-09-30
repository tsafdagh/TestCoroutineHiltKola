package com.android.testcoroutinehiltkola.remote

import androidx.lifecycle.LiveData
import com.android.testcoroutinehiltkola.model.Budget
import com.android.testcoroutinehiltkola.repository.DataSource
import com.android.testcoroutinehiltkola.utils.printlnApp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class RemoteDataSource @Inject constructor(val firebaseFirestore: FirebaseFirestore) : DataSource{

    val BUDGET_COLLECTION= "Budget"
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

    override suspend fun updateBudget(budget: Budget) :Boolean {
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

    override suspend fun getBudgets(): List<Budget> {
        printlnApp("get budgets remotely...")
        return listOf()
    }

}