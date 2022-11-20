package com.android.githubuser.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.githubuser.R
import com.android.githubuser.ui.fragment.SettingPreferenceFragment

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.activity_setting, SettingPreferenceFragment())
            .commit()

        supportActionBar?.title = getString(R.string.title_setting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}