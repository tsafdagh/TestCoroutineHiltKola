package com.android.testcoroutinehiltkola.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.testcoroutinehiltkola.model.Budget
import com.android.testcoroutinehiltkola.model.BudgetTable
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: BudgetTable)

    @Update
    suspend fun update(budget: BudgetTable)

    @Query("SELECT * FROM budgettable")
    fun getAllBudget(): Flow<List<BudgetTable>>

    @Query("SELECT * FROM budgettable")
    suspend fun getCachedBudgets (): List<BudgetTable>

    @Query("SELECT * FROM budgettable")
    fun getCachedBudgetsLiveData (): LiveData<List<BudgetTable>>

}