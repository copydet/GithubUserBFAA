package com.example.githubuserbfaa

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserbfaa.databinding.ListUserItemBinding

class UserAdapter(private val listUser: ArrayList<User>) : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    inner class ListViewHolder(private val binding: ListUserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User){
            with(binding){
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .into(avatarUser)

                namaUser.text = user.name
                followerUser.text = user.totalFollower
                followingUser.text = user.totalFollowing

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])

        //click to detail
        val contextUser = holder.itemView.context

        holder.itemView.setOnClickListener{ Toast.makeText(holder.itemView.context, "You Choose: "+ listUser[holder.adapterPosition].name, Toast.LENGTH_SHORT).show()
            val moveDetail = Intent(contextUser, UserDetail::class.java)
            moveDetail.putExtra(UserDetail.EXTRA_USER, listUser[position])
            moveDetail.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            contextUser.startActivity(moveDetail)
        }
    }

    override fun getItemCount(): Int = listUser.size

}