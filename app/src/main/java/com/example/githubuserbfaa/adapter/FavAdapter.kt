package com.example.githubuserbfaa.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserbfaa.CustomOnItemClickListener
import com.example.githubuserbfaa.R
import com.example.githubuserbfaa.User
import com.example.githubuserbfaa.UserDetail
import com.example.githubuserbfaa.databinding.ListUserItemBinding

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

                //click to Detail Favorite User

                itemView.setOnClickListener(CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback{
                    override fun onItemClicked(v: View, position: Int) {
                        val moveDetail = Intent(activity, UserDetail::class.java)
                        moveDetail.putExtra(UserDetail.EXTRA_POSITION, position)
                        moveDetail.putExtra(UserDetail.EXTRA_USER, user)
                        activity.startActivity(moveDetail)
                    }
                }))

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