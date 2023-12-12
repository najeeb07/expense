package com.najeebappdev.notes.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.najeebappdev.notes.Entity.Expense

@Dao
interface ExpenseDAO {
    @Query("SELECT * FROM expenses")
    fun getAllExpenses(): LiveData<List<Expense>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM expenses WHERE userId = :userId")
    suspend fun getExpensesByUserId(userId: String): List<Expense>

    @Update
    suspend fun update(expense: Expense)

    @Query("SELECT * FROM expenses WHERE userId = :userId")
     fun getExpensesByUserIdLiveData(userId: String): LiveData<List<Expense>>

    @Query("SELECT * FROM expenses")
     fun getAllExpensesLiveData(): LiveData<List<Expense>>
}