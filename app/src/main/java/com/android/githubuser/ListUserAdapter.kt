package com.android.githubuser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.githubuser.databinding.ItemRowUserBinding
import com.bumptech.glide.Glide

class ListUserAdapter(private val listUser: ArrayList<User>) : RecyclerView.Adapter<ListUserAdapter.ListUserHolder>() {
    private lateinit var onItemClickCallBack: OnItemClickCallBack

    fun setOnItemClickCallback(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    class ListUserHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUserHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListUserHolder(binding)
    }

    override fun onBindViewHolder(holder: ListUserHolder, position: Int) {
        val (name, userName, avatar, location) = listUser[position]

        Glide.with(holder.itemView.context)
            .load(avatar)
            .circleCrop()
            .into(holder.binding.ivAvatar)
        holder.binding.apply {
            tvName.text = name
            tvUsername.text = holder.itemView.context.getString(R.string.usernameInParentheses, userName)
            tvLocation.text = location
        }
        holder.itemView.setOnClickListener {
            onItemClickCallBack.onItemClicked(listUser[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    interface OnItemClickCallBack {
        fun onItemClicked(data: User)
    }
}