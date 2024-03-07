package com.bignerdranch.android.expenseapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {

    val allExpenses: LiveData<List<Expense>> = repository.allExpenses
    fun insert(expense: Expense) = viewModelScope.launch {
        repository.insert(expense)
    }
    fun update(expense: Expense) = viewModelScope.launch {
        repository.update(expense)
    }
    val expensesGroupedByCategory: LiveData<List<CategoryWithTotal>> = allExpenses.map { expenses ->
        expenses.groupBy { it.category }
            .map { entry -> CategoryWithTotal(category = entry.key, total = entry.value.sumOf { it.amount }) }
    }
    fun filterExpensesByDate(date: String): LiveData<List<Expense>> {
        return repository.getExpensesByDate(date)
    }
    fun getExpensesByCategory(category: String): LiveData<List<Expense>> {
        return repository.getExpensesByCategory(category)
    }
    fun getExpenseById(id: Int): LiveData<Expense> {
        return repository.getExpenseById(id)
    }
}

data class CategoryWithTotal(val category: String, val total: Double)