package com.example.expensetracker1.data.dao


import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.expensetracker1.data.model.ExpenseModel

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: ExpenseModel)

    @Delete
    suspend fun delete(expense: ExpenseModel)

    @Query("SELECT * FROM expense_table ORDER BY id DESC")
    fun getAllExpenses(): LiveData<List<ExpenseModel>>

    @Query("SELECT SUM(amount) FROM expense_table")
    fun getTotalAmount(): LiveData<Double>
}


