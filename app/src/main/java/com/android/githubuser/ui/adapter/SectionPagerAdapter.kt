package com.android.githubuser.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.githubuser.ui.followers.FollowersFragment
import com.android.githubuser.ui.following.FollowingFragment

class SectionPagerAdapter(fragment: FragmentActivity, data: Bundle) :
    FragmentStateAdapter(fragment) {

    private var fragmentBundle: Bundle = data

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        fragment = when (position) {
            0 -> FollowersFragment()
            1 -> FollowingFragment()
            else -> fragment
        }
        fragment?.arguments = this.fragmentBundle
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return TOTAL_FRAGMENT
    }

    companion object {
        private const val TOTAL_FRAGMENT = 2
    }
}