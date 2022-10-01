package com.android.githubuser.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.android.githubuser.R
import com.android.githubuser.data.local.entity.UserEntity
import com.android.githubuser.databinding.ActivityDetailBinding
import com.android.githubuser.ui.adapter.SectionPagerAdapter
import com.android.githubuser.ui.other.ViewModelFactory
import com.android.githubuser.ui.viewmodel.DetailViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailViewModel: DetailViewModel

    private lateinit var user: UserEntity
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.getParcelableExtra<UserEntity>(EXTRA_USER) as UserEntity
        binding.apply {
            Glide.with(this@DetailActivity)
                .load(user.avatarUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
                .transition(DrawableTransitionOptions.withCrossFade())
                .circleCrop()
                .into(ivAvatarDetail)
            tvNameDetail.text = user.name
            tvFollowersDetail.text = user.followers.toString()
            tvFollowingDetail.text  = user.following.toString()
            tvRepositoryDetail.text = user.repository.toString()
            tvUsernameDetail.text = user.username
            tvLocationDetail.text = user.location
            tvCompanyDetail.text = user.company

            if (user.name.isNullOrEmpty()) tvNameDetail.text = resources.getString(R.string.unset_data)
            if (user.location.isNullOrEmpty()) tvLocationDetail.text = resources.getString(R.string.unset_data)
            if (user.company.isNullOrEmpty()) tvCompanyDetail.text = resources.getString(R.string.unset_data)
        }

        detailViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[DetailViewModel::class.java]
        detailViewModel.isFavorite(user.username).observe(this@DetailActivity) {
            isFavorite = it
            showFavoriteState()
        }

        binding.fabFavorite.setOnClickListener {
            if (isFavorite) {
                detailViewModel.deleteUser(user)
                isFavorite = false
                showFavoriteState()
                showSnackbar(getString(R.string.user_deleted_from_favorite))
            } else {
                detailViewModel.insertUser(user)
                isFavorite = true
                showFavoriteState()
                showSnackbar(getString(R.string.user_added_to_favorite))
            }
        }

        val bundle = Bundle()
        bundle.putString(EXTRA_USER, user.username)

        val sectionViewPagerAdapter = SectionPagerAdapter(this, bundle)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionViewPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)

        TabLayoutMediator(tabs, viewPager) {tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detail User"
    }

    private fun showFavoriteState() {
        if (isFavorite) {
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite_yes)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite_no)
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(findViewById(R.id.activity_detail), message, Snackbar.LENGTH_SHORT)
            .setAction("SEE") {
                val intent = Intent(this, FavoriteActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                startActivity(intent)
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}