package com.android.githubuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.githubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_GithubUser)
        setContentView(binding.root)

        binding.rvUser.setHasFixedSize(true)
        list.addAll(listUsers)
        showRecycleList()
    }

    private val listUsers: ArrayList<User>
        get() {
            val name = resources.getStringArray(R.array.name)
            val userName = resources.getStringArray(R.array.username)
            val avatar = resources.obtainTypedArray(R.array.avatar)
            val location = resources.getStringArray(R.array.location)
            val followers = resources.getStringArray(R.array.followers)
            val following = resources.getStringArray(R.array.following)
            val repository = resources.getStringArray(R.array.repository)
            val company = resources.getStringArray(R.array.company)
            val listUser = ArrayList<User>()

            for (i in name.indices) {
                val user = User(name[i], userName[i], avatar.getResourceId(i, -1), location[i],
                                followers[i], following[i], repository[i], company[i])
                listUser.add(user)
            }
            avatar.recycle()
            return listUser
        }

    private fun showRecycleList() {
        val listUserAdapter = ListUserAdapter(list)

        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listUserAdapter
        }

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: User) {
        val parcelableIntent = Intent(this, DetailActivity::class.java)
        parcelableIntent.putExtra(DetailActivity.EXTRA_USER, user)
        startActivity(parcelableIntent)
    }

}