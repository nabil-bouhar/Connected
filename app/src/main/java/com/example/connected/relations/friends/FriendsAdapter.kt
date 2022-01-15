package com.example.connected.relations.friends

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.connected.databinding.UserItemRealtionsBinding
import com.example.connected.models.User
import com.example.connected.utils.ConnectedUtils
import com.squareup.picasso.Picasso
import javax.inject.Inject

class FriendsAdapter @Inject constructor() : RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {

    var onItemClick: ((User) -> Unit)? = null
    private var users: MutableList<User> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsAdapter.ViewHolder {
        val binding = UserItemRealtionsBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    inner class ViewHolder(private val binding: UserItemRealtionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(users[adapterPosition])
            }
        }

        fun bind(user: User) {
            binding.apply {
                tvPseudo.text = user.pseudo
                binding.tvAge.text = ConnectedUtils.getUserAge(user.birthDate!!)
                binding.tvLocation.text = user.city
                if (user.photo != "default") {
                    Picasso.get().load(user.photo).into(ivProfilePicture)
                }
            }
        }
    }

    fun setUsers(users: MutableList<User>) {
        this.users = users
    }
}