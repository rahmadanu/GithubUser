package com.android.githubuser.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.githubuser.data.remote.response.Items
import com.android.githubuser.databinding.FragmentFollowersFollowingBinding
import com.android.githubuser.ui.activity.DetailActivity
import com.android.githubuser.ui.adapter.ListUserFollowAdapter
import com.android.githubuser.ui.viewmodel.FollowersFollowingViewModel

class FollowingFragment : Fragment() {
    private var _binding: FragmentFollowersFollowingBinding? = null
    private val binding get() = _binding!!
    private val followViewModel by activityViewModels<FollowersFollowingViewModel>()
    private lateinit var listUserFollowAdapter: ListUserFollowAdapter
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowersFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        username = arguments?.getString(DetailActivity.EXTRA_USER).toString()

        showRecycleList()

        followViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        followViewModel.hasNoFollowing.observe(viewLifecycleOwner) {
            showNoData(it)
        }

        followViewModel.findUserFollowing(username)
        followViewModel.getFollowingUser().observe(viewLifecycleOwner) {
            if (it != null) {
                listUserFollowAdapter.setList(it)
            }
        }
    }

    private fun showRecycleList() {
        listUserFollowAdapter = ListUserFollowAdapter()
        listUserFollowAdapter.notifyDataSetChanged()
        binding.apply {
            rvFollowers.layoutManager = LinearLayoutManager(requireContext())
            rvFollowers.setHasFixedSize(true)
            rvFollowers.adapter = listUserFollowAdapter
        }
        listUserFollowAdapter.setOnItemClickCallback(object : ListUserFollowAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: Items) {
                Toast.makeText(requireActivity(), data.username, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showNoData(hasNoData: Boolean) {
        binding.tvDataNotFound.visibility = if (hasNoData) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}