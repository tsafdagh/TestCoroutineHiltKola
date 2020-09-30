package com.android.testcoroutinehiltkola.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
data class CashFlowTable (

    @ColumnInfo(name = "id")
    @SerializedName("id")
    @PrimaryKey(autoGenerate = false)
    var id: String,

    @ColumnInfo(name = "category")
    @SerializedName("category")
    var category: String,

    @ColumnInfo(name = "date")
    @SerializedName("date")
    var date: Long,

    @ColumnInfo(name = "image")
    @SerializedName("image")
    var image: String,

    @ColumnInfo(name = "note")
    @SerializedName("note")
    var note: String,

    @ColumnInfo(name = "amount")
    @SerializedName("amount")
    var amount: Double,

    @ColumnInfo(name = "uri")
    @SerializedName("uri")
    var uri : String = "",

    @ColumnInfo(name = "future")
    @SerializedName("future")
    var future : Boolean = false,

    @ColumnInfo(name = "createdDate")
    @SerializedName("createdDate")
    val createdDate : Long = Date().time,

    @ColumnInfo(name = "dateNotification")
    @SerializedName("dateNotification")
    var dateNotification : Long = Date().time,

    @ColumnInfo(name = "budjetized")
    @SerializedName("budjetized")
    var budjetized: String = "",

    @ColumnInfo(name = "isFromTransaction")
    @SerializedName("isFromTransaction")
    var isFromTransaction : Boolean = false
){
}