package com.najeebappdev.notes.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.najeebappdev.notes.Dao.ExpenseDAO
import com.najeebappdev.notes.Entity.Expense
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class ExpenseRepository(private val expenseDAO: ExpenseDAO, private val firebaseDatabase: FirebaseDatabase) {


    val allExpenses: LiveData<List<Expense>> = expenseDAO.getAllExpensesLiveData()


    suspend fun insert(expense: Expense) {
        expenseDAO.insertExpense(expense)
    }

    suspend fun update(expense: Expense) {
        expenseDAO.updateExpense(expense)
    }

    suspend fun delete(expense: Expense) {
        expenseDAO.deleteExpense(expense)
    }

    fun getTodayTotalExpenses(): LiveData<Double> {
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        return Transformations.map(expenseDAO.getAllExpenses()) { expenses ->
            expenses
                .filter { it.timestamp >= today.timeInMillis }
                .sumByDouble { it.amount }
        }

    }



    suspend fun getExpensesByUserId(userId: String): LiveData<List<Expense>> {
        return expenseDAO.getExpensesByUserIdLiveData(userId)
    }



    // Firebase Realtime Database synchronization
    suspend fun syncExpenses(userId: String) {
        val firebaseExpenses = firebaseDatabase.getReference("expenses/$userId")

        firebaseExpenses.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val expenses = mutableListOf<Expense>()

                    for (expenseSnapshot in snapshot.children) {
                        val expense = expenseSnapshot.getValue(Expense::class.java)
                        expense?.let { expenses.add(it) }
                    }

                    CoroutineScope(Dispatchers.IO).launch {
                        // Update local database with fetched expenses
                        expenses.forEach { expenseDAO.update(it) }
                    }
                } catch (e: Exception) {
                    Log.e("FirebaseSync", "Error processing data: ${e.message}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Log.e("FirebaseSync", "Data synchronization cancelled: ${error.message}")
            }
        })
    }





}