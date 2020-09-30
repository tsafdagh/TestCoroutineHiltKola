package com.android.testcoroutinehiltkola.model

import android.os.Parcelable
import com.android.testcoroutinehiltkola.enums.RANGBUDGET
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.util.*

@Parcelize
data class Budget(
    var id: String,
    var name: String,
    var rang: RANGBUDGET,
    var budgetAmount: Double,
    var startedDate: Date,
    val createdDate: Date,
    var reccurence: String,
    var notification: Boolean,
    var active: Boolean = false,
    var listStartedDate: MutableList<Date> = mutableListOf(Date()),
    var listCashflow: MutableList<CashFlow> = mutableListOf()
) : Parcelable {
    constructor() : this("", "", RANGBUDGET.BUDGET1, 0.0, Date(), Date(), "", false)

    fun amountSpend(): Double {
        var amount = 0.0

        val cal = Calendar.getInstance()
        cal.time = this.startedDate


        val list = mutableListOf<CashFlow>()

        listCashflow.forEach {
            val calDate = Calendar.getInstance()
            calDate.time = it.date
            if (!it.future && cal.get(Calendar.DATE) <= calDate.get(Calendar.DATE)) {
                list.add(it)
            }
        }

        list.forEach {
            amount += it.amount
        }

        return amount
    }

    fun listCashflowInCurrentPeriod(): MutableList<CashFlow> {

        val list = mutableListOf<CashFlow>()

        if (this.listStartedDate.size != 0) {
            val cal = Calendar.getInstance()
            cal.time = this.listStartedDate.get(listStartedDate.size - 1)

            listCashflow.forEach {
                val calDate = Calendar.getInstance()
                calDate.time = it.date
                if (!it.future && cal.get(Calendar.DATE) <= calDate.get(Calendar.DATE)) {
                    list.add(it)
                }
            }
        }

        return list
    }

}