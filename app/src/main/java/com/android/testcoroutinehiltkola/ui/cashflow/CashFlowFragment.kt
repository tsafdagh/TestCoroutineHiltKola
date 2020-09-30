package com.android.testcoroutinehiltkola.ui.cashflow

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.testcoroutinehiltkola.R

class CashFlowFragment : Fragment() {

    companion object {
        fun newInstance() =
            CashFlowFragment()
    }

    private lateinit var viewModel: CashFlowViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cash_flow_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CashFlowViewModel::class.java)
        // TODO: Use the ViewModel
    }

}