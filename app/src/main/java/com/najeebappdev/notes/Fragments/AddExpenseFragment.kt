package com.najeebappdev.notes.Fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.najeebappdev.notes.Entity.Expense
import com.najeebappdev.notes.R
import com.najeebappdev.notes.ViewModel.ExpenseViewModel
import com.najeebappdev.notes.databinding.FragmentAddExpenseBinding
import java.util.Locale

class AddExpenseFragment : Fragment(R.layout.fragment_add_expense) {

    private lateinit var binding: FragmentAddExpenseBinding
    private lateinit var expenseViewModel: ExpenseViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddExpenseBinding.bind(view)
        expenseViewModel = ViewModelProvider(requireActivity()).get(ExpenseViewModel::class.java)



        binding.addButton.setOnClickListener {
//            val title = binding.titleEditText.text.toString()
            val selectedInterest = binding.expenseTypeSpinner.selectedItem.toString()

            val amount = binding.amountEditText.text.toString().toDouble()

            if (selectedInterest.isNotEmpty()) {
                val user = FirebaseAuth.getInstance().currentUser
                val userId = user?.uid

                if (userId != null) {
                    val expense = Expense(title = selectedInterest, amount = amount, userId = userId)
                    expenseViewModel.insert(expense)

                    // Clear input fields after adding expense
                    binding.amountEditText.text.clear()
                } else {
                    // Handle the case where the user is not authenticated
                    // You might want to redirect the user to the login screen or show an error message
                    // For simplicity, you can print a message for now
                    println("User is not authenticated")
                }
            }
        }


    }
}
