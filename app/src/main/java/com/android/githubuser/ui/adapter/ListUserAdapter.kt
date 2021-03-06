package com.android.githubuser.ui.adapter

import android.content.Intent
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.githubuser.R
import com.android.githubuser.data.local.entity.UserEntity
import com.android.githubuser.databinding.ItemRowUserBinding
import com.android.githubuser.ui.activity.DetailActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import okhttp3.internal.notifyAll

class ListUserAdapter(private val onFavoriteClick: (UserEntity) -> Unit) : ListAdapter<UserEntity, ListUserAdapter.UserViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        val ivFavorite = holder.binding.ivFavorite

        if (user.isFavorite) {
            ivFavorite.setImageDrawable(ContextCompat.getDrawable(ivFavorite.context, R.drawable.ic_favorite_yes))
        } else {
            ivFavorite.setImageDrawable(ContextCompat.getDrawable(ivFavorite.context, R.drawable.ic_favorite_no))
        }

        ivFavorite.setOnClickListener {
            onFavoriteClick(user)
        }
    }

    class UserViewHolder(val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user: UserEntity) {
            binding.apply {
                tvUsername.text = user.username
                tvUrl.text = user.url
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .circleCrop()
                    .into(ivAvatar)
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USER, user)
                itemView.context.startActivity(intent)
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
}