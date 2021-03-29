package com.example.githubuserbfaa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserbfaa.databinding.ActivityMainBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val list = ArrayList<User>()

    companion object{
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Github User"

        binding.rvUser.setHasFixedSize(true)


        showRecyclerList()
        getListUser()
        search()
    }

    private fun getListUser() {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 5c0f980b23aecb26456df0f341506e7e6d41c5ff")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users"
        client.get(url, object : AsyncHttpResponseHandler(){

            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)

                    for (i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)

                        // Get Parsing DETAIL USER

                        val detailUser = jsonObject.getString("url")
                        client.get(detailUser, object : AsyncHttpResponseHandler() {
                            override fun onSuccess(
                                statusCode: Int,
                                headers: Array<Header>,
                                responseBody: ByteArray
                            ) {
                                val resultDetail = String(responseBody)
                                Log.d(TAG, resultDetail)
                                try {

                                    //json Detail User
                                    val jsonObjectDetail = JSONObject(resultDetail)
                                    val username = jsonObjectDetail.getString("login").toString()
                                    val avatar = jsonObjectDetail.getString("avatar_url")
                                    val name = jsonObjectDetail.getString("name").toString()
                                    val company = jsonObjectDetail.getString("company")
                                    val followers = jsonObjectDetail.getString("followers")
                                    val following = jsonObjectDetail.getString("following")
                                    val location = jsonObjectDetail.getString("location")
                                    val blog = jsonObjectDetail.getString("blog")

                                    // Show Recycle View List
                                    val users = User()
                                    users.avatar = avatar
                                    users.totalFollower = "Followers: $followers"
                                    users.totalFollowing = "Following: $following"
                                    users.company = company
                                    users.username = username
                                    users.name = name
                                    users.location = location
                                    users.blog = blog
                                    list.add(users)

                                    showRecyclerList()
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        e.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    e.printStackTrace()
                                }
                            }

                            override fun onFailure(
                                statusCode: Int,
                                headers: Array<Header>,
                                responseBody: ByteArray,
                                error: Throwable
                            ) {
                                binding.progressBar.visibility = View.INVISIBLE
                                val errorMessageDetail = when (statusCode) {
                                    401 -> "$statusCode : permintaan buruk"
                                    403 -> "$statusCode : tidak di ketahui"
                                    404 -> "$statusCode : tidak ditemukan"
                                    else -> "$statusCode : ${error.message}"
                                }
                                Toast.makeText(
                                    this@MainActivity,
                                    errorMessageDetail,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                    }
                } catch (e: Exception){
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when(statusCode) {
                    401 -> "$statusCode : permintaan buruk"
                    403 -> "$statusCode : tidak di ketahui"
                    404 -> "$statusCode : tidak ditemukan"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showRecyclerList(){
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = UserAdapter(list)
        binding.rvUser.adapter = listUserAdapter
    }

    private fun search(){ binding.searchUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            if (query.isEmpty()) {
                return true
            } else {
                list.clear()
                searchUser(query)
            }
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            return false
        }
    })
    }
    private fun searchUser(id: String){
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 5c0f980b23aecb26456df0f341506e7e6d41c5ff")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/search/users?q=$id"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONObject(result)
                    val item = jsonArray.getJSONArray("items")
                    for (i in 0 until item.length()) {
                        val jsonObject = item.getJSONObject(i)

                        // detail user search
                        val detailSearch = jsonObject.getString("url")
                        client.get(detailSearch, object : AsyncHttpResponseHandler() {
                            override fun onSuccess(
                                statusCode: Int,
                                headers: Array<Header>,
                                responseBody: ByteArray
                            ) {
                                val resultDetail = String(responseBody)
                                Log.d(TAG, resultDetail)
                                try {

                                    //json Detail User
                                    val jsonObjectDetail = JSONObject(resultDetail)
                                    val username = jsonObjectDetail.getString("login")
                                    val avatar = jsonObjectDetail.getString("avatar_url")
                                    val name = jsonObjectDetail.getString("name").toString()
                                    val company = jsonObjectDetail.getString("company")
                                    val followers = jsonObjectDetail.getString("followers")
                                    val following = jsonObjectDetail.getString("following")
                                    val location = jsonObjectDetail.getString("location")
                                    val blog = jsonObjectDetail.getString("blog")

                                    // Show Recycle View List
                                    val users = User()
                                    users.avatar = avatar
                                    users.totalFollower = "Followers: $followers"
                                    users.totalFollowing = "Following: $following"
                                    users.company = company
                                    users.username = username
                                    users.name = name
                                    users.location = "Location: $location"
                                    users.blog = "Blog: $blog"
                                    list.add(users)

                                    showRecyclerList()
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        e.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    e.printStackTrace()
                                }
                            }

                            override fun onFailure(
                                statusCode: Int,
                                headers: Array<Header>,
                                responseBody: ByteArray,
                                error: Throwable
                            ) {
                                binding.progressBar.visibility = View.INVISIBLE
                                val errorMessageDetail = when (statusCode) {
                                    401 -> "$statusCode : permintaan buruk"
                                    403 -> "$statusCode : tidak di ketahui"
                                    404 -> "$statusCode : tidak ditemukan"
                                    else -> "$statusCode : ${error.message}"
                                }
                                Toast.makeText(
                                    this@MainActivity,
                                    errorMessageDetail,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT)
                        .show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {

                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : permintaan buruk"
                    403 -> "$statusCode : tidak di ketahui"
                    404 -> "$statusCode : tidak ditemukan"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    // Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setMode(selectedMode: Int) {
        when(selectedMode){
            R.id.favorite_list -> {
                val favorite = Intent(this, FavoriteActivity::class.java)
                startActivity(favorite)
            }
            R.id.set_alarm ->{
                val alarm = Intent(this, ReminderSetting::class.java)
                startActivity(alarm)
            }
        }

    }
}