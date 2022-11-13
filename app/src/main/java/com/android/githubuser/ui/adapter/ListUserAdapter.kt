package com.android.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.githubuser.databinding.ItemRowUserBinding
import com.android.githubuser.network.model.Items
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class ListUserAdapter
    : RecyclerView.Adapter<ListUserAdapter.ListUserHolder>() {

    private lateinit var onItemClickCallBack: OnItemClickCallBack

    fun setOnItemClickCallback(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Items>() {
        override fun areItemsTheSame(oldItem: Items, newItem: Items): Boolean {
            return oldItem.username == newItem.username
        }

        @Suppress("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Items, newItem: Items): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun setList(user: ArrayList<Items>) {
        differ.submitList(user)
    }

    inner class ListUserHolder(private val binding: ItemRowUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: Items) {
            binding.apply {
                Glide.with(itemView)
                    .load(user.avatarUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .circleCrop()
                    .into(ivAvatar)
                tvUsername.text = user.username
                tvLink.text = user.link
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
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    interface OnItemClickCallBack {
        fun onItemClicked(data: Items)
    }
}