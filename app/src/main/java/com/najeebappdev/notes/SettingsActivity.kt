package com.najeebappdev.notes

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.najeebappdev.notes.ViewModel.ExpenseViewModel
import com.najeebappdev.notes.databinding.ActivitySettingsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySettingsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var expenseViewModel: ExpenseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        auth = FirebaseAuth.getInstance()
        expenseViewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)



        binding.syncButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val userId = auth.currentUser?.uid
                userId?.let {
                    expenseViewModel.syncExpenses(it)
                    Toast.makeText(this@SettingsActivity, "Syncing...", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.darkModeSwitch.isChecked = DarkModeUtil.isDarkModeEnabled(this)

        binding.darkModeSwitch.setOnCheckedChangeListener { ge, isChecked ->
            DarkModeUtil.saveDarkModePreference(this, isChecked)
            DarkModeUtil.applyDarkMode(this)
        }


    }

}