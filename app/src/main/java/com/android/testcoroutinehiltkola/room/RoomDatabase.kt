package com.android.testcoroutinehiltkola.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.testcoroutinehiltkola.model.BudgetTable

@Database(entities = [BudgetTable::class], version = 1)
abstract class RoomDatabase : RoomDatabase(){

    abstract fun budgetDao() : BudgetDao

    companion object{
        val DATABASE_NAME : String = "database_budget"
    }
}