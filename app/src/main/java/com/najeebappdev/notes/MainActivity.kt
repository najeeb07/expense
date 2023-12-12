package com.najeebappdev.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.najeebappdev.notes.Adapters.ViewPagerAdapter
import com.najeebappdev.notes.Fragments.AddExpenseFragment
import com.najeebappdev.notes.Fragments.ShowTodayExpensesFragment
import com.najeebappdev.notes.ViewModel.ExpenseViewModel
import com.najeebappdev.notes.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        auth = FirebaseAuth.getInstance()

        val fragments = listOf(
            AddExpenseFragment(),
            ShowTodayExpensesFragment()
        )

        val adapter = ViewPagerAdapter(this, fragments)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Add Expense"
                1 -> tab.text = "Today's Expenses"
            }
        }.attach()

        expenseViewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.action_settings -> {
                // Handle Settings option

                val settingsIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingsIntent)
                return true
            }
            R.id.action_share -> {
                // Handle Share option

                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                val shareText = "Check out this amazing app!"
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
                startActivity(Intent.createChooser(shareIntent, "Share via"))
                return true
            }
            R.id.action_signout -> {
                // Handle Rate option

                auth.signOut()
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


}