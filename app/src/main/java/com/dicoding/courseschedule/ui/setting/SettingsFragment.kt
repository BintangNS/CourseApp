package com.dicoding.courseschedule.ui.setting

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.preference.SwitchPreferenceCompat
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.notification.DailyReminder
import com.dicoding.courseschedule.util.NightMode

class SettingsFragment : PreferenceFragmentCompat() {
    private val dailyReminder = DailyReminder()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        Log.d("SettingsFragment", "onCreatePreferences called") // logging for testing
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val themePreference = findPreference<ListPreference>("key_dark_mode")
        themePreference?.setOnPreferenceChangeListener { _, newValue ->
            val nightMode = when (newValue) {
                NightMode.ON.name -> NightMode.ON.value
                NightMode.OFF.name -> NightMode.OFF.value
                else -> NightMode.AUTO.value
            }
            updateTheme(nightMode)
            true
        }

        val notificationsPreference = findPreference<SwitchPreference>("key_notification")
        Log.d("SettingsFragment", "notificationsPreference: $notificationsPreference")
        notificationsPreference?.setOnPreferenceChangeListener { _, newValue ->
            if (!(newValue as Boolean)) {
                Log.d("SettingsFragment", "Switch turned off")
                dailyReminder.cancelAlarm(requireContext())
            }
            true
        }
    }

    private fun updateTheme(nightMode: Int) {
        Handler(Looper.getMainLooper()).postDelayed({
            if (isAdded) {
                AppCompatDelegate.setDefaultNightMode(nightMode)
                requireActivity().recreate()
            }
        }, 200)
    }
}