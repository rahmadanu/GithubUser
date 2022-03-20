package com.android.githubuser.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionPagerAdapter(fragment: FragmentActivity, data: Bundle) : FragmentStateAdapter(fragment) {

    private var fragmentBundle: Bundle = data

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        fragment = when(position) {
            0 -> FollowersFragment()
            1 -> FollowingFragment()
            else -> fragment
        }
        fragment?.arguments = this.fragmentBundle
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }

}