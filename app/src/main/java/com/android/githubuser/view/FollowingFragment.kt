package com.android.githubuser.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.githubuser.MainViewModel
import com.android.githubuser.databinding.FragmentFollowersFollowingBinding
import com.android.githubuser.model.Items

class FollowingFragment : Fragment() {
    private var _binding: FragmentFollowersFollowingBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by activityViewModels<MainViewModel>()
    private lateinit var listUserAdapter: ListUserAdapter
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

        mainViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        mainViewModel.hasNoFollowing.observe(viewLifecycleOwner) {
            showNoData(it)
        }

        mainViewModel.findUserFollowing(username)
        mainViewModel.getFollowingUser().observe(viewLifecycleOwner) {
            if (it != null) {
                listUserAdapter.setList(it)
            }
        }
    }

    private fun showRecycleList() {
        listUserAdapter = ListUserAdapter()
        listUserAdapter.notifyDataSetChanged()
        binding.apply {
            rvFollowers.layoutManager = LinearLayoutManager(requireContext())
            rvFollowers.setHasFixedSize(true)
            rvFollowers.adapter = listUserAdapter
        }
        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallBack {
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