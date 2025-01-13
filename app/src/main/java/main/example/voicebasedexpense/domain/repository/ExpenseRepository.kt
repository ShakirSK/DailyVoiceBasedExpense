package main.example.voicebasedexpense.domain.repository

import androidx.lifecycle.LiveData
import main.example.voicebasedexpense.data.local.dao.ExpenseDao
import main.example.voicebasedexpense.data.model.Expense

class ExpenseRepository(private val expenseDao: ExpenseDao) {

    val allExpenses: LiveData<List<Expense>> = expenseDao.getAllExpense()

    suspend fun insert(expense: Expense) {
        expenseDao.insertExpense(expense)
    }

    suspend fun delete(expense: Expense) {
        expenseDao.deleteExpense(expense)
    }
}