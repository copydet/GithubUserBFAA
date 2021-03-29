package com.example.githubuserbfaa

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserbfaa.databinding.FragmentFollowerBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class FollowerFragment : Fragment() {
    private lateinit var adapter : UserAdapter
    private var list : ArrayList<User> = ArrayList()
    private lateinit var binding: FragmentFollowerBinding


    companion object {
        const val FLR_USERNAME = "username"
        private val TAG = FollowerFragment::class.java.simpleName

        fun newInstance(userId: String): FollowerFragment {
            val fragment = FollowerFragment()
            val bundle = Bundle()
            bundle.putString(FLR_USERNAME, userId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFollowerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataFollower: RecyclerView = binding.dataFollower

        dataFollower.setHasFixedSize(true)
        list = ArrayList()
        adapter = UserAdapter(list)

        dataFollower.layoutManager = LinearLayoutManager(requireContext())
        dataFollower.adapter = adapter

        val usernameD = arguments?.getString(FLR_USERNAME)
        Log.d("FollowerFragment",usernameD.toString())
        getUsername(usernameD.toString())
    }

    private fun getUsername(id: String) {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$id/followers"
        client.addHeader("Authorization", "token 5c0f980b23aecb26456df0f341506e7e6d41c5ff")
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                binding.progressBar.visibility =View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG,result)

                try {
                    val jsonArray = JSONArray(result)

                    for (i in 0 until jsonArray.length()){
                        val jsonObjects = jsonArray.getJSONObject(i)
                        val usernameFlr = jsonObjects.getString("login")
                        dataDetailFlr(usernameFlr)

                    }
                }catch (e: Exception){
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when(statusCode) {
                    401 -> "$statusCode : permintaan buruk"
                    403 -> "$statusCode : tidak di ketahui"
                    404 -> "$statusCode : tidak ditemukan"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

    }
    private fun dataDetailFlr(id: String){
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 5c0f980b23aecb26456df0f341506e7e6d41c5ff")
        val url = "https://api.github.com/users/$id"
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                val result = String(responseBody)
                try {
                    val jsonObject = JSONObject(result)
                    val username = jsonObject.getString("login")
                    val name = jsonObject.getString("name")
                    val avatar = jsonObject.getString("avatar_url")
                    val totalflr = jsonObject.getString("followers")
                    val totalflw = jsonObject.getString("following")
                    val blog = jsonObject.getString("blog")
                    val company = jsonObject.getString("company")
                    val location = jsonObject.getString("location")

                    val detailFlr = User()
                    detailFlr.avatar = avatar
                    detailFlr.username = username
                    detailFlr.name = name
                    detailFlr.totalFollower = "Follower: $totalflr"
                    detailFlr.totalFollowing = "Following: $totalflw"
                    detailFlr.company = company
                    detailFlr.location = location
                    detailFlr.blog = blog

                    list.add(detailFlr)

                    showRecycleList()
                }catch (e: java.lang.Exception){
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {

            }
        })
    }

    private fun showRecycleList() {
        binding.dataFollower.layoutManager = LinearLayoutManager(activity)
        binding.dataFollower.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}