package com.android.testcoroutinehiltkola.repository

import androidx.lifecycle.LiveData
import com.android.testcoroutinehiltkola.model.Budget
import kotlinx.coroutines.flow.Flow

interface DataSource {

    fun createBudget (Budget: Budget) : Flow<Boolean>

    suspend fun updateBudget (Budget: Budget) :Boolean

    suspend fun getBudgets (): List<Budget>
}