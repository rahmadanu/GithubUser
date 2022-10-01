package com.android.githubuser

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.android.githubuser.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide


class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        binding.apply {
            Glide.with(binding.root)
                .load(user.avatar)
                .circleCrop()
                .into(ivAvatarDetail)
            tvNameDetail.text = user.name
            tvFollowersDetail.text = user.followers
            tvFollowingDetail.text = user.following
            tvRepositoryDetail.text = user.repository
            tvUsernameDetail.text = user.userName
            tvLocationDetail.text = user.location
            tvCompanyDetail.text = user.company
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.share -> {
                val sendUserDetails = getString(
                    R.string.user_details,
                    binding.tvNameDetail.text,
                    binding.tvUsernameDetail.text,
                    binding.tvFollowersDetail.text,
                    binding.tvFollowingDetail.text,
                    binding.tvRepositoryDetail.text,
                    binding.tvLocationDetail.text,
                    binding.tvCompanyDetail.text
                )

                val intent = Intent(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_TEXT, sendUserDetails)
                    .setType("text/plain")

                startActivity(Intent.createChooser(intent, "Send user details"))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EXTRA_USER = "extra_user"
    }
}