package com.android.githubuser.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.githubuser.R
import com.android.githubuser.databinding.ActivityFavoriteBinding
import com.android.githubuser.ui.adapter.ListUserAdapter
import com.android.githubuser.ui.viewmodel.FavoriteViewModel
import com.android.githubuser.ui.other.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {

    private var _binding: ActivityFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var listUserAdapter: ListUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        favoriteViewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

        listUserAdapter = ListUserAdapter()

        favoriteViewModel.getFavoriteUser().observe(this@FavoriteActivity) { favoriteUser ->
            if (favoriteUser.isNullOrEmpty()) {
                showNoData(true)
            } else {
                showNoData(false)
            }
            listUserAdapter.submitList(favoriteUser)
        }

        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = listUserAdapter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Favorite User"
    }

    private fun showNoData(hasData: Boolean) {
        binding.tvNoData.visibility = if (hasData) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}