package com.android.testcoroutinehiltkola.model

import android.os.Parcelable
import com.android.testcoroutinehiltkola.enums.ENUMCATEGORYEXPENSE
import kotlinx.android.parcel.Parcelize
import java.util.*
import java.io.Serializable

@Parcelize
data class CashFlow(
    var id: String,
    var category: ENUMCATEGORYEXPENSE?,
    var date: Date,
    var image: String,
    var note: String,
    var amount: Double,
    var uri: String = "",
    var future: Boolean = false,
    val createdDate: Date = Date(),
    var dateNotification: Date = Date(),
    var budjetized: String = "",
    var isFromTransaction: Boolean = false
) : Parcelable {

    init {
        if (category == null) {
            category = ENUMCATEGORYEXPENSE.UNKNOWN
        }
//        id = "${this.createdDate.time}${this.hashCode()}"
    }

    constructor() : this("", null, Date(), "", "", 0.0)

    override fun equals(other: Any?): Boolean {
        if (other is CashFlow) {
            return other.createdDate.equals(this.createdDate)
        } else {
            return false
        }
    }
}