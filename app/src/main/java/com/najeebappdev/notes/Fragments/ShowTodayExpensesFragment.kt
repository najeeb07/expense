package com.najeebappdev.notes.Fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.najeebappdev.notes.Adapters.ExpenseAdapter
import com.najeebappdev.notes.Entity.Expense
import com.najeebappdev.notes.R
import com.najeebappdev.notes.ViewModel.ExpenseViewModel
import com.najeebappdev.notes.databinding.DialogEditExpenseBinding
import com.najeebappdev.notes.databinding.FragmentShowTodayExpensesBinding
import java.util.Calendar
import java.util.Locale

class ShowTodayExpensesFragment : Fragment(R.layout.fragment_show_today_expenses) {

    private lateinit var binding: FragmentShowTodayExpensesBinding
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var expenseAdapter: ExpenseAdapter



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentShowTodayExpensesBinding.bind(view)
        expenseViewModel = ViewModelProvider(requireActivity()).get(ExpenseViewModel::class.java)
        val expenseAdapter = ExpenseAdapter { expense -> showEditDialog(expense) }

        binding.todayExpensesRecyclerView.adapter = expenseAdapter
        binding.todayExpensesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        expenseViewModel.allExpenses.observe(viewLifecycleOwner, { expenses ->
            val todayExpenses = expenses.filter { isToday(it.timestamp) }
            expenseAdapter.setExpenses(todayExpenses)
        })


        expenseViewModel.todayTotalExpenses.observe(requireActivity(), { totalExpenses ->
            binding.totalExpensesTextView.text = String.format(Locale.getDefault(), "Today's Total: %.2f", totalExpenses)
        })


    }

    private fun isToday(timestamp: Long): Boolean {
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        return timestamp >= today.timeInMillis
    }

        private fun showEditDialog(expense: Expense) {
        val builder = AlertDialog.Builder(requireActivity())
            var dialogBinding = DialogEditExpenseBinding.inflate(layoutInflater)
            builder.setView(dialogBinding.root)

        val alertDialog = builder.create()

        dialogBinding.editTitleEditText.setText(expense.title)
        dialogBinding.editAmountEditText.setText(expense.amount.toString())

        dialogBinding.btnSave.setOnClickListener {
            val newTitle = dialogBinding.editTitleEditText.text.toString()
            val newAmount = dialogBinding.editAmountEditText.text.toString().toDouble()

            if (newTitle.isNotEmpty()) {
                val updatedExpense = expense.copy(title = newTitle, amount = newAmount)
                expenseViewModel.update(updatedExpense)
            }

            alertDialog.dismiss()
        }

        dialogBinding.btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}