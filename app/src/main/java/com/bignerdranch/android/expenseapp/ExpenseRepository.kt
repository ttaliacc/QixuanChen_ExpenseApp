package com.bignerdranch.android.expenseapp

import androidx.lifecycle.LiveData

class ExpenseRepository(private val query: Query) {
    val allExpenses: LiveData<List<Expense>> = query.getAllExpenses()

    fun getExpensesByCategory(category: String): LiveData<List<Expense>> {
        return query.getExpensesByCategory(category)
    }
    fun getExpenseById(id: Int): LiveData<Expense> {
        return query.getExpenseById(id)
    }
    fun getExpensesByDate(date: String): LiveData<List<Expense>> {
        return query.getExpensesByDate(date)
    }
    suspend fun insert(expense: Expense) {
        query.insert(expense)
    }
    suspend fun update(expense: Expense) {
        query.update(expense)
    }

}
