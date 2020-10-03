package com.android.testcoroutinehiltkola.ui.budget

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.android.testcoroutinehiltkola.model.Budget
import com.android.testcoroutinehiltkola.repository.Repository
import com.android.testcoroutinehiltkola.ui.budget.BudgetStateEvent.*
import com.android.testcoroutinehiltkola.utils.Resource
import com.android.testcoroutinehiltkola.utils.printlnApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class BudgetViewModel
@ExperimentalCoroutinesApi
@ViewModelInject constructor(
    private val repository: Repository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _dataState: MutableLiveData<Resource<List<Budget>>> = MutableLiveData()

    val dataState: LiveData<Resource<List<Budget>>>
        get() = _dataState



    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    val myTestFlowLivedata:LiveData<Resource<List<Budget>>> = repository.getBudget2().asLiveData()

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    fun setStateEvent(budgetStateEvent: BudgetStateEvent) {
        viewModelScope.launch {
            when (budgetStateEvent) {
                is GetBudgetsEvent -> {
  /*                  _dataState.value = Resource.Loading(null)

                    printlnApp("viewModel get budgets...")
                    repository.getCachedBudgets().onEach { dataState ->
                        _dataState.value = dataState
                    }.launchIn(viewModelScope)

                    repository.getSynckBudgetRealTime().onEach { dataState ->
                        _dataState.value = dataState
                    }.launchIn(viewModelScope)*/

                    repository.getBudget2().collect {
                        _dataState.value  = it
                    }
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

    class CreateBudgetEvent(val budget: Budget) : BudgetStateEvent()

    class UpdateBudgetEvent(val budget: Budget) : BudgetStateEvent()
}