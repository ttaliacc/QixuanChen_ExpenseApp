package com.bignerdranch.android.expenseapp

import android.app.Application
import com.bignerdranch.android.expenseappÏ.DataBase

class ExpenseCreater : Application() {
    val repository: ExpenseRepository by lazy {
        val expenseDao = DataBase.getDatabase(this).Query()
        ExpenseRepository(expenseDao)
    }
}
