// ExpensesAdapter.kt
package com.bignerdranch.android.expenseapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.expenseapp.databinding.ItemExpenseBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ExpenseAdapter(private val onItemClicked: (Expense) -> Unit) :
    ListAdapter<Expense, ExpenseAdapter.ExpenseViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ItemExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpenseViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val currentExpense = getItem(position)
        holder.bind(currentExpense)
    }
    class ExpenseViewHolder(private val binding: ItemExpenseBinding, private val onItemClicked: (Expense) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(expense: Expense) {
            binding.apply {
                expenseDate.text = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(expense.date)
//                expenseDate.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(expense.date)
                expenseAmount.text = String.format("$%.2f", expense.amount)
                expenseCategory.text = expense.category
                root.setOnClickListener {
                    onItemClicked(expense)
                }
            }
        }
    }
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Expense>() {
            override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean = oldItem == newItem
        }
    }
}
