package com.android.githubuser.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.android.githubuser.ui.main.MainViewModel
import com.android.githubuser.R
import com.android.githubuser.databinding.ActivityDetailBinding
import com.android.githubuser.ui.adapter.SectionPagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.isLoadingDetail.observe(this) {
            showLoading(it)
        }

        mainViewModel.hasConnectionFailed.observe(this) {
            showConnectionFailed(it)
        }
        val user = intent.getStringExtra(EXTRA_USER)
        val bundle = Bundle()
        bundle.putString(EXTRA_USER, user.toString())

        mainViewModel.findUserDetail(user!!)
        mainViewModel.getDetailUser().observe(this) {
            binding.apply {
                if (it != null) {
                    Glide.with(this@DetailActivity)
                        .load(it.avatarUrl)
                        .circleCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(ivAvatarDetail)
                    tvNameDetail.text = it.name
                    tvFollowersDetail.text = it.followers.toString()
                    tvFollowingDetail.text  = it.following.toString()
                    tvRepositoryDetail.text = it.repository.toString()
                    tvUsernameDetail.text = it.username
                    tvLocationDetail.text = it.location
                    tvCompanyDetail.text = it.company

                    if (it.name.isNullOrEmpty()) tvNameDetail.text = resources.getString(R.string.unset_data)
                    if (it.location.isNullOrEmpty()) tvLocationDetail.text = resources.getString(R.string.unset_data)
                    if (it.company.isNullOrEmpty()) tvCompanyDetail.text = resources.getString(R.string.unset_data)
                }
            }
        }
        val sectionViewPagerAdapter = SectionPagerAdapter(this, bundle)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionViewPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)

        TabLayoutMediator(tabs, viewPager) {tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showConnectionFailed(hasNoData: Boolean) {
        binding.tvConnectionFailed.visibility = if (hasNoData) View.VISIBLE else View.GONE
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