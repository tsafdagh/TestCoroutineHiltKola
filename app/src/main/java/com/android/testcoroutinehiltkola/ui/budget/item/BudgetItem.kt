package com.android.testcoroutinehiltkola.ui.budget.item

import android.content.res.ColorStateList
import android.view.View
import androidx.navigation.Navigation
import com.android.testcoroutinehiltkola.R
import com.android.testcoroutinehiltkola.model.Budget
import com.android.testcoroutinehiltkola.utils.convertDateToSpecificStringFormat
import com.android.testcoroutinehiltkola.utils.formatNumberWithSpaceBetweenThousand
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.row_item_budget_main.view.*
import java.util.*

class BudgetItem (val budget: Budget): Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
//        if (!budget.active) {
//            viewHolder.itemView.id_budget_item_ll_full_budget.visibility = View.GONE
////            viewHolder.itemView.setOnClickListener{
////                val action = BudgetMainFragmentDirections.actionBudgetMainFragmentToCreateBudgetFragment(budget)
////                Navigation.findNavController(it).navigate(action)
////            }
//        } else {
            viewHolder.itemView.id_budget_item_tv_add_budget.visibility = View.GONE

            viewHolder.itemView.id_background_textview.backgroundTintList =
                ColorStateList.valueOf(viewHolder.itemView.context.resources.getColor(budget.rang.color))
            viewHolder.itemView.id_budget_item_tv_amount_spend.backgroundTintList =
                ColorStateList.valueOf(viewHolder.itemView.context.resources.getColor(budget.rang.color))

            viewHolder.itemView.id_budget_item_tv_budget_name.text = budget.name

            if (budget.name.length > 3) {
                viewHolder.itemView.id_budget_item_tv_name_abr.text = budget.name.substring(0, 3)
            } else {
                viewHolder.itemView.id_budget_item_tv_name_abr.text = budget.name
            }

            viewHolder.itemView.id_budget_item_tv_amount_total.text =
                budget.budgetAmount.formatNumberWithSpaceBetweenThousand()


            val startedDate = Calendar.getInstance()
            startedDate.time = budget.startedDate
            var amount = budget.amountSpend()
            viewHolder.itemView.id_budget_item_tv_amount_spend.text =
                amount.formatNumberWithSpaceBetweenThousand()
            viewHolder.itemView.id_budget_tv_start_date.text =
                startedDate.time.convertDateToSpecificStringFormat("dd MMM")
            startedDate.add(Calendar.DAY_OF_MONTH, 30)
            viewHolder.itemView.id_budget_tv_end_date.text =
                startedDate.time.convertDateToSpecificStringFormat("dd MMM")

//        }
    }

    override fun getLayout()= R.layout.row_item_budget_main
}