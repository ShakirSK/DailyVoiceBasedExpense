package main.example.voicebasedexpense.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import main.example.voicebasedexpense.data.model.Expense

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insertExpense(expense: Expense)

    @Query("Select * from expenses ORDER BY date DESC")
    fun getAllExpense(): LiveData<List<Expense>>

    @Delete
    suspend fun deleteExpense(expense: Expense)
}