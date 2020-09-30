package com.android.testcoroutinehiltkola.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class BudgetTable (
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @PrimaryKey(autoGenerate = false)
    val id: String,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    val name: String,

    @ColumnInfo(name = "rang")
    @SerializedName("rang")
    var rang: String,

    @ColumnInfo(name = "budgetAmount")
    @SerializedName("budgetAmount")
    val budgetAmount: Double,

    @ColumnInfo(name = "startedDate")
    @SerializedName("startedDate")
    val startedDate: Long,

    @ColumnInfo(name = "createdDate")
    @SerializedName("createdDate")
    val createdDate: Long,

    @ColumnInfo(name = "reccurence")
    @SerializedName("reccurence")
    val reccurence: String,

    @ColumnInfo(name = "notification")
    @SerializedName("notification")
    val notification: Boolean,

    @ColumnInfo(name = "active")
    @SerializedName("active")
    val active: Boolean
) {
}