package me.daffakurnia.android.githubusers

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.daffakurnia.android.githubusers.API.ApiConfig
import me.daffakurnia.android.githubusers.DataClass.DataUser
import me.daffakurnia.android.githubusers.Response.ItemsItem
import me.daffakurnia.android.githubusers.Response.UserSearchResponse
import me.daffakurnia.android.githubusers.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerUser: RecyclerView
    private var list = ArrayList<DataUser>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.listUsers.layoutManager = layoutManager

        findUsers()
    }

    private fun findUsers() {
        val client = ApiConfig.getApiService().getAccount(USERNAME)
        client.enqueue(object : Callback<UserSearchResponse> {
            override fun onResponse(
                call: Call<UserSearchResponse>,
                response: Response<UserSearchResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setSearchData(responseBody.items)
                    }
                } else {
                    Log.e(this@MainActivity.toString(), "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
                Log.e(this@MainActivity.toString(), "onFailure: ${t.message}")
            }

        })
    }

    private fun setSearchData(items: List<ItemsItem?>?) {
        val userList = ArrayList<DataUser>()

        if (items != null) {
            for (item in items) {
                userList.add(
                    DataUser(
                        item?.login,
                        item?.htmlUrl,
                        item?.avatarUrl
                    )
                )
            }
        }
        val adapter = ListUserAdapter(userList)
        binding.listUsers.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search_input)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_placeholder)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                USERNAME = query
                findUsers()
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    companion object {
        private var USERNAME = "username"
    }
}