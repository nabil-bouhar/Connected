package com.example.connected.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.connected.databinding.UserCardHomeBinding
import com.example.connected.models.User
import com.example.connected.utils.ConnectedUtils
import com.squareup.picasso.Picasso
import javax.inject.Inject

class HomeAdapter @Inject constructor(): RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    var onItemClick: ((User) -> Unit)? = null
    private var users: MutableList<User> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.ViewHolder {
        val binding =
            UserCardHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    inner class ViewHolder(private val binding: UserCardHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(users[adapterPosition])
            }
        }

        fun bind(user: User) {
            binding.apply {
                tvDisplayName.text = user.pseudo
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