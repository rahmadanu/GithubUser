package com.android.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.githubuser.R
import com.android.githubuser.databinding.ItemRowUserBinding
import com.android.githubuser.data.remote.response.Items
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlin.collections.ArrayList

class ListUserFollowAdapter : RecyclerView.Adapter<ListUserFollowAdapter.ListUserHolder>() {

    private lateinit var onItemClickCallBack: OnItemClickCallBack

    fun setOnItemClickCallback(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    private val list = ArrayList<Items>()

    fun setList(user: ArrayList<Items>?) {
        list.clear()
        if (user != null) {
            list.addAll(user)
        }
        notifyDataSetChanged()
    }

    inner class ListUserHolder(private val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: Items) {
            binding.apply {
                Glide.with(itemView)
                    .load(user.avatarUrl)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .circleCrop()
                    .into(ivAvatar)
                tvUsername.text = user.username
                tvUrl.text = user.url
                ivFavorite.visibility = View.GONE
            }
            binding.root.setOnClickListener {
                onItemClickCallBack.onItemClicked(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUserHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListUserHolder(binding)
    }

    override fun onBindViewHolder(holder: ListUserHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClickCallBack {
        fun onItemClicked(data: Items)
    }

}