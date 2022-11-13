package com.android.githubuser.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.githubuser.R
import com.android.githubuser.databinding.ActivityMainBinding
import com.android.githubuser.network.model.Items
import com.android.githubuser.ui.adapter.ListUserAdapter
import com.android.githubuser.ui.detail.DetailActivity

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var listUserAdapter: ListUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_GithubUser)
        setContentView(binding.root)

        showRecycleList()

        mainViewModel.getListUser().observe(this@MainActivity) {
            if (it != null) {
                listUserAdapter.setList(it)
                Log.d(TAG, "items: $it")
            }
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.hasNoData.observe(this) {
            showNoData(it)
        }

        mainViewModel.hasConnectionFailed.observe(this) {
            Toast.makeText(this, R.string.connection_lost, Toast.LENGTH_LONG).show()
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
                mainViewModel.findUsername(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem): Boolean {
                with(binding) {
                    tvNoData.visibility = View.GONE
                    rvUser.visibility = View.VISIBLE
                }
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem): Boolean {
                with(binding) {
                    rvUser.visibility = View.GONE
                    tvNoData.visibility = View.VISIBLE
                    tvNotFoundData.visibility = View.GONE
                    progressBar.visibility = View.GONE
                }
                return true
            }
        })
        return true
    }

    private fun showRecycleList() {
        listUserAdapter = ListUserAdapter()
        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = listUserAdapter
        }
        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: Items) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: Items) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_USER, user.username)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showNoData(hasNoData: Boolean) {
        binding.tvNotFoundData.visibility = if (hasNoData) View.VISIBLE else View.GONE
        binding.tvNoData.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}