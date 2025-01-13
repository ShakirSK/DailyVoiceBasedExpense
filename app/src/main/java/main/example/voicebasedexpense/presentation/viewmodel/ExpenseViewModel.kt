package main.example.voicebasedexpense.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import main.example.voicebasedexpense.data.model.Expense
import main.example.voicebasedexpense.data.local.database.ExpenseDatabase
import main.example.voicebasedexpense.domain.repository.ExpenseRepository

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {


    private val repository: ExpenseRepository
    val allExpenses: LiveData<List<Expense>>


    init {
        val expenseDao = ExpenseDatabase.getDatabase(application).expenseDao()
        repository = ExpenseRepository(expenseDao)
        allExpenses = repository.allExpenses
    }

    fun calculateTotalAmount(expenses: List<Expense>): Double {
        return expenses.sumOf { it.amount }
    }

    fun insert(expense: Expense) = viewModelScope.launch {
        repository.insert(expense)
    }

    fun delete(expense: Expense) = viewModelScope.launch {
        repository.delete(expense)
    }





   /* private val expenseDao = ExpenseDatabase.getDatabase(application).expenseDao()

    fun insertExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseDao.insertExpense(expense)
        }
    }

    fun getAllExpenses(): LiveData<List<Expense>> {
        return expenseDao.getAllExpense()
    }*/
}