package main.example.voicebasedexpense.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import main.example.voicebasedexpense.data.model.Expense
import main.example.voicebasedexpense.data.local.dao.ExpenseDao

@Database(entities = [Expense::class], version = 1 , exportSchema = false)
abstract class ExpenseDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao

    companion object{

        private var INSTANCE: ExpenseDatabase? =  null

        fun getDatabase(context:Context): ExpenseDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java,
                    "expense_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}