package main.example.voicebasedexpense.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
class Expense (
    @PrimaryKey(autoGenerate = true) val id:Int =0,
            val amount: Double,
    val category: String,
    val description: String,
    val date: Long = System.currentTimeMillis()
)