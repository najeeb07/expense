package com.najeebappdev.notes

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

object DarkModeUtil {
    private const val PREF_DARK_MODE = "pref_dark_mode"
    private const val PREF_DARK_MODE_DEFAULT = false

    fun applyDarkMode(context: Context) {
        val darkModePref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            .getBoolean(PREF_DARK_MODE, PREF_DARK_MODE_DEFAULT)
        AppCompatDelegate.setDefaultNightMode(
            if (darkModePref) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    fun saveDarkModePreference(context: Context, isDarkMode: Boolean) {
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE).edit()
            .putBoolean(PREF_DARK_MODE, isDarkMode).apply()
    }


    fun isDarkModeEnabled(context: Context): Boolean {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            .getBoolean(PREF_DARK_MODE, PREF_DARK_MODE_DEFAULT)
    }
}
