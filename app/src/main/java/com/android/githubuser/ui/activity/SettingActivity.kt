package com.android.githubuser.ui.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.android.githubuser.R
import com.android.githubuser.ui.fragment.SettingPreferenceFragment
import com.android.githubuser.ui.other.SettingPreference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Settings")

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.activity_setting, SettingPreferenceFragment())
            .commit()

        supportActionBar?.title = "Setting"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}