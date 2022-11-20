package com.android.githubuser.ui.adapter

import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.githubuser.R
import com.android.githubuser.data.local.entity.UserEntity
import com.android.githubuser.databinding.ItemRowUserBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

class ListUserAdapter(val context: Context) :
    ListAdapter<UserEntity, ListUserAdapter.UserViewHolder>(
        DIFF_CALLBACK
    ) {
    private var listener: OnItemClickListener? = null

    fun setOnClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user, context)
    }

    inner class UserViewHolder(private val binding: ItemRowUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserEntity, context: Context) {
            binding.apply {
                tvUsername.text = user.username
                tvUrl.text = user.url

                when (context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        Glide.with(itemView.context)
                            .load(user.avatarUrl)
                            .apply(
                                RequestOptions.placeholderOf(R.drawable.ic_loading)
                                    .error(R.drawable.ic_error)
                            )
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .placeholder(R.color.grey_800)
                            .circleCrop()
                            .into(ivAvatar)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        Glide.with(itemView.context)
                            .load(user.avatarUrl)
                            .apply(
                                RequestOptions.placeholderOf(R.drawable.ic_loading)
                                    .error(R.drawable.ic_error)
                            )
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .circleCrop()
                            .into(ivAvatar)
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {}
                }
            }
            binding.root.setOnClickListener {
                listener?.onItemClicked(user)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<UserEntity> =
            object : DiffUtil.ItemCallback<UserEntity>() {
                override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
                    return oldItem.username == newItem.username
                }

                @Suppress("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
                    return oldItem == newItem
                }

            }
    }

    interface OnItemClickListener {
        fun onItemClicked(user: UserEntity)
    }
}