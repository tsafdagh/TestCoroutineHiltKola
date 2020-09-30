package com.android.testcoroutinehiltkola.room

import androidx.room.*
import com.android.testcoroutinehiltkola.model.BudgetTable

@Dao
interface BudgetDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(budget: BudgetTable)

    @Update
    suspend fun update(budget: BudgetTable)

    @Query("SELECT * FROM budgettable")
    suspend fun getAllBudget(): List<BudgetTable>
}