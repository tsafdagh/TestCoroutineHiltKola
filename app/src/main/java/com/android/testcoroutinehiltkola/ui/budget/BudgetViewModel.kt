package com.android.testcoroutinehiltkola.ui.budget

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.android.testcoroutinehiltkola.model.Budget
import com.android.testcoroutinehiltkola.repository.Repository
import com.android.testcoroutinehiltkola.ui.budget.BudgetStateEvent.*
import com.android.testcoroutinehiltkola.utils.DataState
import com.android.testcoroutinehiltkola.utils.printlnApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class BudgetViewModel
@ViewModelInject constructor(
    private val repository: Repository,
    @Assisted private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _dataState : MutableLiveData<DataState<List<Budget>>> = MutableLiveData()

    val dataState :LiveData<DataState<List<Budget>>>
        get() = _dataState

    fun setStateEvent (budgetStateEvent: BudgetStateEvent){
        viewModelScope.launch {
            when(budgetStateEvent){
                is GetBudgetsEvent -> {
                    printlnApp("viewModel get budgets...")
                    repository.getBudgets().onEach {dataState->
                        _dataState.value = dataState
                    }.launchIn(viewModelScope)
                }

                is CreateBudgetEvent -> {
                    printlnApp("viewModel create budgets...")
                    repository.createBudget(budgetStateEvent.budget)
                }

                is UpdateBudgetEvent -> {
                    printlnApp("viewModel update budgets...")
                    repository.updateBudget(budgetStateEvent.budget)
                }
            }
        }
    }

}

sealed class BudgetStateEvent {

    object GetBudgetsEvent : BudgetStateEvent()

    class CreateBudgetEvent (val budget: Budget) : BudgetStateEvent()

    class UpdateBudgetEvent (val budget: Budget) : BudgetStateEvent()
}