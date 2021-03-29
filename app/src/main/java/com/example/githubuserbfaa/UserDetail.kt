package com.example.githubuserbfaa

import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.githubuserbfaa.databinding.ActivityUserDetailBinding
import com.example.githubuserbfaa.db.UserContract.UserColumns.Companion.AVATAR
import com.example.githubuserbfaa.db.UserContract.UserColumns.Companion.BLOG
import com.example.githubuserbfaa.db.UserContract.UserColumns.Companion.COMPANY
import com.example.githubuserbfaa.db.UserContract.UserColumns.Companion.CONTENT_URI
import com.example.githubuserbfaa.db.UserContract.UserColumns.Companion.FAVORITE
import com.example.githubuserbfaa.db.UserContract.UserColumns.Companion.LOCATION
import com.example.githubuserbfaa.db.UserContract.UserColumns.Companion.NAME
import com.example.githubuserbfaa.db.UserContract.UserColumns.Companion.TOTALFLR
import com.example.githubuserbfaa.db.UserContract.UserColumns.Companion.TOTALFLW
import com.example.githubuserbfaa.db.UserContract.UserColumns.Companion.USERNAME
import com.example.githubuserbfaa.db.UserHelper
import com.google.android.material.tabs.TabLayout

class UserDetail : AppCompatActivity(), View.OnClickListener{
    private var isFavorite = false
    private lateinit var favoriteHelper: UserHelper
    private lateinit var avatarImg : String


    private lateinit var binding: ActivityUserDetailBinding

    companion object{
        const val EXTRA_USER = "extra_user"
        const val EXTRA_POSITION = "extra_position"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Detail User'S"


        val user = intent.getParcelableExtra(EXTRA_USER) as User?
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        user?.username?.let { sectionsPagerAdapter.setUsername(it) }
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        favoriteHelper = UserHelper.getInstance(applicationContext)
        favoriteHelper.open()
        val cursor: Cursor = favoriteHelper.queryById(user?.username.toString())
        if (cursor.moveToNext()){
            isFavorite = true
            setStatusFavorite(true)
        }


        detailUser()
        binding.fav.setOnClickListener(this)

    }

    private fun detailUser() {
        val dataDetailUser = intent.getParcelableExtra<User>(EXTRA_USER)
        binding.namaUser.text = dataDetailUser?.name.toString()
        binding.company.text = dataDetailUser?.company.toString()
        binding.location.text = dataDetailUser?.location.toString()
        binding.blogUser.text = dataDetailUser?.blog.toString()
        binding.username.text = dataDetailUser?.username.toString()
        Glide.with(this)
                .load(dataDetailUser?.avatar)
                .into(binding.avatarUser)
        avatarImg = dataDetailUser?.avatar.toString()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onClick(v: View) {
        val userf = intent.getParcelableExtra(EXTRA_USER) as User?
        when (v.id){
            R.id.fav -> {
                if (isFavorite) {
                    favoriteHelper.deleteById(userf?.username.toString())

                    Toast.makeText(this, "Dihapus dari Favorit", Toast.LENGTH_SHORT).show()
                    isFavorite = true
                    setStatusFavorite(false)
                } else {

                        Toast.makeText(this, "Ditambah ke Favorite", Toast.LENGTH_SHORT).show()
                        val name = binding.namaUser.text.toString()
                        val username = binding.username.text.toString()
                        val location = binding.location.text.toString()
                        val company = binding.company.text.toString()
                        val favoriteData = "1"
                        Glide.with(this)
                                .load(userf?.avatar)
                                .into(binding.avatarUser)
                        avatarImg = userf?.avatar.toString()
                        val avatarFav = avatarImg
                        val totalflr = userf?.totalFollower.toString()
                        val totalflw = userf?.totalFollowing.toString()
                        val blogfav = userf?.blog.toString()


                        val values = ContentValues()
                        values.put(USERNAME, username)
                        values.put(NAME, name)
                        values.put(AVATAR, avatarFav)
                        values.put(LOCATION, location)
                        values.put(COMPANY, company)
                        values.put(FAVORITE, favoriteData)
                        values.put(BLOG, blogfav)
                        values.put(TOTALFLR, totalflr)
                        values.put(TOTALFLW, totalflw)

                        contentResolver.insert(CONTENT_URI, values)

                        isFavorite = false
                        setStatusFavorite(true)
                }
            }
        }
    }
    private fun setStatusFavorite(b: Boolean) {
        if (b){
            binding.fav.setImageResource(R.drawable.ic_baseline_favorite_24)
        }else {
            binding.fav.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteHelper.close()
    }
}
