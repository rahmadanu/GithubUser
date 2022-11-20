package com.android.githubuser.ui.adapter

import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.githubuser.R
import com.android.githubuser.data.remote.response.Items
import com.android.githubuser.databinding.ItemRowUserBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

class ListUserFollowAdapter(val context: Context) :
    RecyclerView.Adapter<ListUserFollowAdapter.ListUserHolder>() {

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

    inner class ListUserHolder(private val binding: ItemRowUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: Items, context: Context) {
            binding.apply {
                when (context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        Glide.with(itemView)
                            .load(user.avatarUrl)
                            .circleCrop()
                            .apply(
                                RequestOptions.placeholderOf(R.drawable.ic_loading)
                                    .error(R.drawable.ic_error)
                            )
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .placeholder(R.color.grey_800)
                            .into(ivAvatar)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        Glide.with(itemView)
                            .load(user.avatarUrl)
                            .circleCrop()
                            .apply(
                                RequestOptions.placeholderOf(R.drawable.ic_loading)
                                    .error(R.drawable.ic_error)
                            )
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(ivAvatar)
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {}
                }
                tvUsername.text = user.username
                tvUrl.text = user.url
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
        holder.bind(list[position], context)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClickCallBack {
        fun onItemClicked(data: Items)
    }

}