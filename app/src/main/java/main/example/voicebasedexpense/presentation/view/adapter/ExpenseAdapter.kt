package main.example.voicebasedexpense.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import main.example.voicebasedexpense.R
import main.example.voicebasedexpense.data.model.Expense
import java.text.SimpleDateFormat
import java.util.*

class ExpenseAdapter : ListAdapter<Expense, ExpenseAdapter.ExpenseViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = getItem(position)
        holder.bind(expense)
    }

    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(expense: Expense) {
            itemView.findViewById<TextView>(R.id.textAmount).text = "₹${expense.amount}"
            itemView.findViewById<TextView>(R.id.textCategory).text = expense.category
            itemView.findViewById<TextView>(R.id.textDescription).text = expense.description
            itemView.findViewById<TextView>(R.id.textDate).text =
                SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(expense.date))
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Expense, newItem: Expense) = oldItem == newItem
    }
}