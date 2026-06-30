package com.example.expensetracker1.uI.adapter



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.expensetracker1.data.model.ExpenseModel
import com.example.expensetracker1.databinding.ExpenseItemBinding

class ExpenseAdapter(private val onDeleteClick: (ExpenseModel) -> Unit)
    : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    private var expenses = listOf<ExpenseModel>()

    inner class ExpenseViewHolder(val binding: ExpenseItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ExpenseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        holder.binding.apply {
            tvTitle.text = expense.title
            tvCategory.text = expense.category
            tvAmount.text = "$${String.format("%,.2f", expense.amount)}"
            tvDate.text = expense.date
        }

        holder.itemView.setOnLongClickListener {
            onDeleteClick(expense)
            true
        }
        holder.binding.tvDelete.setOnClickListener {
            onDeleteClick(expense)
        }

    }

    override fun getItemCount() = expenses.size

    fun setExpenses(list: List<ExpenseModel>) {
        expenses = list
        notifyDataSetChanged()
    }
}