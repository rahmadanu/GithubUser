package com.android.githubuser.ui.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.githubuser.R
import com.android.githubuser.di.Result
import com.android.githubuser.databinding.ActivityMainBinding
import com.android.githubuser.ui.adapter.ListUserAdapter
import com.android.githubuser.ui.other.SettingPreference
import com.android.githubuser.ui.other.SettingViewModelFactory
import com.android.githubuser.ui.viewmodel.MainViewModel
import com.android.githubuser.ui.other.ViewModelFactory
import com.android.githubuser.ui.viewmodel.SettingViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Settings")

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel

    private lateinit var settingViewModel: SettingViewModel

    private lateinit var listUserAdapter: ListUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_GithubUser)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        showRecycleList()
        val pref = SettingPreference.getInstance(dataStore)
        settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]
        settingViewModel.getThemeSetting().observe(this) {
                isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.top_app_bar_main, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchMenuItem = menu.findItem(R.id.search)
        val searchView = searchMenuItem.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.getUser(query).observe(this@MainActivity) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                showLoading(true)
                                showNoData(false)
                            }
                            is Result.Success -> {
                                showLoading(false)
                                val userData = result.data
                                if (userData.isNullOrEmpty()) {
                                    showNoData(true)
                                    Toast.makeText(this@MainActivity, "User tidak ditemukan", Toast.LENGTH_SHORT).show()
                                } else {
                                    showNoData(false)
                                }
                                listUserAdapter.submitList(userData)
                            }
                            is Result.Error -> {
                                showLoading(true)
                                Toast.makeText(this@MainActivity, R.string.connection_failed, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        searchMenuItem.setOnActionExpandListener(object: MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                binding.tvNoData.visibility = View.GONE
                binding.ivGithubUserLogo.visibility = View.GONE
                binding.rvUser.visibility = View.VISIBLE
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                binding.rvUser.visibility = View.GONE
                binding.tvNoData.visibility = View.VISIBLE
                binding.ivGithubUserLogo.visibility = View.VISIBLE
                binding.tvNotFoundData.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
            }
            R.id.setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

   private fun showRecycleList() {
       listUserAdapter = ListUserAdapter { user ->
           if (user.isFavorite) {
               mainViewModel.deleteUser(user)
           } else {
               mainViewModel.insertUser(user)
           }
           listUserAdapter.notifyDataSetChanged() //saya terpaksa pakai notifyDataSetChanged() karena DiffUtil tidak bisa bekerja
       }
       binding.rvUser.layoutManager = LinearLayoutManager(this)
       binding.rvUser.setHasFixedSize(true)
       binding.rvUser.adapter = listUserAdapter
   }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showNoData(hasNoData: Boolean) {
        binding.tvNotFoundData.visibility = if (hasNoData) View.VISIBLE else View.GONE
        binding.ivGithubUserLogo.visibility = if (hasNoData) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}