package com.android.testcoroutinehiltkola.ui.budget

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.android.testcoroutinehiltkola.R
import com.android.testcoroutinehiltkola.enums.RANGBUDGET
import com.android.testcoroutinehiltkola.model.Budget
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_create_budget.*
import java.util.*

@AndroidEntryPoint
class CreateBudgetFragment : Fragment() {

    private lateinit var viewModel: BudgetViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_budget, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BudgetViewModel::class.java)

        id_btn_edit_budget.setOnClickListener {
            createBudget()
        }
    }

    fun createBudget(){
        val stateEvent = BudgetStateEvent.CreateBudgetEvent(Budget(
            Date().toString(),
            id_edit_budget_name.text.toString(),
            RANGBUDGET.BUDGET3,
            id_edit_amount_budget.text.toString().toDouble(),
            Date(),
            Date(),
            "Monthly",
            true,
            true,
            mutableListOf(),
            mutableListOf()
        ))
        viewModel.setStateEvent(stateEvent)
    }

    companion object {
        fun newInstance() =
            CreateBudgetFragment()

    }
}