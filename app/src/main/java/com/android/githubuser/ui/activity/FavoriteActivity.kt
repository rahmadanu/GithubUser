package com.android.githubuser.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.githubuser.R
import com.android.githubuser.data.local.entity.UserEntity
import com.android.githubuser.databinding.ActivityFavoriteBinding
import com.android.githubuser.ui.adapter.ListUserAdapter
import com.android.githubuser.ui.other.ViewModelFactory
import com.android.githubuser.ui.viewmodel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {

    private var _binding: ActivityFavoriteBinding? = null
    private val binding get() = _binding
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var listUserAdapter: ListUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        favoriteViewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

        listUserAdapter = ListUserAdapter(this@FavoriteActivity)

        favoriteViewModel.getFavoriteUser().observe(this@FavoriteActivity) { favoriteUser ->
            if (favoriteUser.isNullOrEmpty()) {
                showNoData(true)
            } else {
                showNoData(false)
            }
            listUserAdapter.submitList(favoriteUser)
        }

        binding?.apply {
            rvUser.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = listUserAdapter
        }

        listUserAdapter.setOnClickListener(object : ListUserAdapter.OnItemClickListener {
            override fun onItemClicked(user: UserEntity) {
                val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USER, user)
                startActivity(intent)
            }
        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.title_favorite_user)
    }

    private fun showNoData(hasData: Boolean) {
        binding?.tvNoData?.visibility = if (hasData) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}