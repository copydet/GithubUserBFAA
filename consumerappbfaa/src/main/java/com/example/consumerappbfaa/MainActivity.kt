package com.example.consumerappbfaa

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerappbfaa.adapter.FavAdapter
import com.example.consumerappbfaa.databinding.ActivityFavoriteBinding
import com.example.consumerappbfaa.db.UserContract
import com.example.consumerappbfaa.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: FavAdapter
    private lateinit var binding: ActivityFavoriteBinding

    companion object{
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite User"

        binding.rvFavuser.layoutManager = LinearLayoutManager(this)
        binding.rvFavuser.setHasFixedSize(true)
        adapter = FavAdapter(this)
        binding.rvFavuser.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val observer = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadUserAsync()
            }
        }
        contentResolver.registerContentObserver(UserContract.UserColumns.CONTENT_URI, true, observer)

        if (savedInstanceState == null){
            loadUserAsync()
        }else{
            val list = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
            if (list != null){
                adapter.listFavorite = list
            }
        }

    }

    private fun loadUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            val defferedUser = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(UserContract.UserColumns.CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorArrayList(cursor)
            }
            val favoriteData = defferedUser.await()
            binding.progressBar.visibility = View.INVISIBLE
            if (favoriteData.size > 0){
                adapter.listFavorite = favoriteData
            }else {
                adapter.listFavorite = ArrayList()
                showSnackBarMessage()
            }
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorite)
    }

    private fun showSnackBarMessage() {
        Toast.makeText(this, "Tambahkan Favorite Users", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadUserAsync()
    }
}