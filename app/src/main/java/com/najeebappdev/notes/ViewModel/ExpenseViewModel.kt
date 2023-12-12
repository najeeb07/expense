package com.najeebappdev.notes.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.Calendar
import com.google.firebase.database.FirebaseDatabase
import com.najeebappdev.notes.Entity.Expense
import com.najeebappdev.notes.Database.ExpenseDatabase
import com.najeebappdev.notes.Repository.ExpenseRepository


class ExpenseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ExpenseRepository
    val allExpenses: LiveData<List<Expense>>
    val todayTotalExpenses: LiveData<Double>

    init {
        val expenseDAO = ExpenseDatabase.getDatabase(application).expenseDAO()
        repository = ExpenseRepository(expenseDAO, FirebaseDatabase.getInstance())
        allExpenses = repository.allExpenses
        todayTotalExpenses = Transformations.map(repository.allExpenses) { expenses ->
            calculateTodayTotalExpenses(expenses)
        }


    }

    // Local Database operations (SQLite)
    suspend  fun getExpensesByUserId(userId: String): LiveData<List<Expense>> {
        return repository.getExpensesByUserId(userId)
    }

    // Firebase Realtime Database synchronization
    suspend fun syncExpenses(userId: String) {
        repository.syncExpenses(userId)
    }

    private fun calculateTodayTotalExpenses(expenses: List<Expense>): Double {
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        return expenses
            .filter { it.timestamp >= today.timeInMillis }
            .sumByDouble { it.amount }
    }

    fun insert(expense: Expense) = viewModelScope.launch {
        repository.insert(expense)
    }

    fun update(expense: Expense) = viewModelScope.launch {
        repository.update(expense)
    }

    fun delete(expense: Expense) = viewModelScope.launch {
        repository.delete(expense)
    }
}