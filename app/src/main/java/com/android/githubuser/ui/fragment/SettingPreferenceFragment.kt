package com.android.githubuser.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.android.githubuser.R
import com.android.githubuser.ui.other.SettingPreference
import com.android.githubuser.ui.other.SettingViewModelFactory
import com.android.githubuser.ui.viewmodel.SettingViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Settings")

class SettingPreferenceFragment: PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var DARK: String

    private lateinit var darkModePreference: SwitchPreference

    private lateinit var settingViewModel: SettingViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.setting_preference)
        init()
        setSummaries()
    }


    private fun init() {
        DARK = resources.getString(R.string.key_dark_mode)

        darkModePreference = findPreference<SwitchPreference>(DARK) as SwitchPreference

        val pref = SettingPreference.getInstance(requireActivity().dataStore)
        settingViewModel = ViewModelProvider(requireActivity(), SettingViewModelFactory(pref))[SettingViewModel::class.java]
        settingViewModel.getThemeSetting().observe(requireActivity()) {
                isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                darkModePreference.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                darkModePreference.isChecked = false
            }
        }

        darkModePreference.setOnPreferenceClickListener {
            if (darkModePreference.isChecked) {
                settingViewModel.saveThemeSetting(true)
            } else {
                settingViewModel.saveThemeSetting(false)
            }
            return@setOnPreferenceClickListener true
        }

    }

    private fun setSummaries() {
        val spInstance = preferenceManager.sharedPreferences!!
        darkModePreference.isChecked = spInstance.getBoolean(DARK, false)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sp: SharedPreferences, key: String) {
        if (key == DARK) {
            darkModePreference.isChecked = sp.getBoolean(DARK, false)
        }
    }
}