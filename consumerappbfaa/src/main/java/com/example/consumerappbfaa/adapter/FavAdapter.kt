package com.example.consumerappbfaa.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.consumerappbfaa.R
import com.example.consumerappbfaa.User
import com.example.consumerappbfaa.databinding.ListUserItemBinding

class FavAdapter(private val activity: Activity) : RecyclerView.Adapter<FavAdapter.FavViewHolder>() {
    var listFavorite = ArrayList<User>()
        set(listFavorite) {
            if (listFavorite.size > 0) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)

            notifyDataSetChanged()
        }

    inner class FavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListUserItemBinding.bind(itemView)
        fun bind(user: User) {
            with(binding) {
                Glide.with(itemView.context)
                        .load(user.avatar)
                        .into(avatarUser)

                namaUser.text = user.name
                followerUser.text = user.totalFollower
                followingUser.text = user.totalFollowing

            }
        }
    }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_user_item, parent, false)
            return FavViewHolder(view)
        }
    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }
        override fun getItemCount(): Int = this.listFavorite.size

}