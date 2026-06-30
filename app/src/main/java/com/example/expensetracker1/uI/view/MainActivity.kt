package com.example.expensetracker1.uI.view

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker1.R
import com.example.expensetracker1.data.model.ExpenseModel
import com.example.expensetracker1.databinding.ActivityMainBinding
import com.example.expensetracker1.databinding.DialogAddExpenseBinding
import com.example.expensetracker1.uI.adapter.ExpenseAdapter
import com.example.expensetracker1.uI.viewModel.ExpenseViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.widget.ArrayAdapter
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ExpenseViewModel
    private lateinit var adapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
//        val tvTotal = findViewById<TextView>(R.id.tvTotal)
//        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)

        adapter = ExpenseAdapter { expense ->
            viewModel.delete(expense)
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]

        viewModel.allExpenses.observe(this) {
            adapter.setExpenses(it)
        }

        viewModel.totalAmount.observe(this) { total ->
            binding.tvTotal.text = "Total: $${String.format("%.2f", total ?: 0.0)}"
        }

        binding.fabAdd.setOnClickListener {
            showAddDialog()
        }
    }

    private fun showAddDialog() {
        val dialogBinding = DialogAddExpenseBinding.inflate(layoutInflater)
        val categories = arrayOf("Food", "Travel", "Shopping", "Bills", "Education", "Other")
        val categoryAdapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, categories)
        dialogBinding.etCategory.setAdapter(categoryAdapter)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Expense")
            .setView(dialogBinding.root)
            .setPositiveButton("Add") { _, _ ->
                val title = dialogBinding.etTitle.text.toString().trim()
                val amountText = dialogBinding.etAmount.text.toString()

                if(title.isEmpty() || amountText.isEmpty()){
                    Toast.makeText(this,"Enter all fields",Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val amount = amountText.toDouble()

                if(amount <= 0){
                    Toast.makeText(this,"Amount must be greater than zero",Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val category = dialogBinding.etCategory.text.toString()
                val date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())

                val expense = ExpenseModel(title = title, amount = amount, category = category, date = date)
                viewModel.insert(expense)
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()


    }
}