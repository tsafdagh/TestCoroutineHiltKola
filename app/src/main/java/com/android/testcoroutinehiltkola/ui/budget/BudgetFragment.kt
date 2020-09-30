package com.android.testcoroutinehiltkola.ui.budget

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.testcoroutinehiltkola.R
import com.android.testcoroutinehiltkola.enums.RANGBUDGET
import com.android.testcoroutinehiltkola.model.Budget
import com.android.testcoroutinehiltkola.ui.budget.item.BudgetItem
import com.android.testcoroutinehiltkola.utils.DataState
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.budget_fragment.*
import kotlinx.android.synthetic.main.budget_fragment.view.*
import java.util.*

@AndroidEntryPoint
class BudgetFragment : Fragment() {

    companion object {
        fun newInstance() =
            BudgetFragment()
    }

    private lateinit var viewModel: BudgetViewModel
    private lateinit var budgetRecyclerView : RecyclerView
    private var shouldInitRecyclerView = true
    private lateinit var section: Section
    private var amountSpend = 0.0
    private var amountTotal = 1.0
    private lateinit var groupAdapter : GroupAdapter<ViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.budget_fragment, container, false)
        budgetRecyclerView = root.id_list_budget
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BudgetViewModel::class.java)
        // TODO: Use the ViewModel

        val budgetStateEvent = BudgetStateEvent.GetBudgetsEvent
        viewModel.setStateEvent(budgetStateEvent)

        id_btn_create_budget.setOnClickListener{
            findNavController().navigate(R.id.action_budgetFragment_to_createBudgetFragment)
        }

        viewModel.dataState.observe(viewLifecycleOwner, Observer {dataState->
            when(dataState){
                is DataState.Success<List<Budget>> -> {
                    val items = mutableListOf<Item>()
                    dataState.data.forEach {
                        items.add(BudgetItem(it))
                    }
                    updateRecyclerView(items)
                }

                is DataState.Error -> {

                }

                is DataState.Loading -> {

                }
            }

        })
    }

    private fun updateRecyclerView (items: MutableList<Item>){

        fun init(){
            groupAdapter = GroupAdapter<ViewHolder>().apply {
                section = Section(items)
                add(section)
            }
            budgetRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@BudgetFragment.context, LinearLayoutManager.VERTICAL, false)
                adapter = groupAdapter
                setHasFixedSize(true)
            }
            //should init recyclerview why this ?
            //shouldInitRecyclerView = false
        }

        fun updateItems() = section.update(items)

        if(shouldInitRecyclerView)
            init()
        else
            updateItems()
    }

}