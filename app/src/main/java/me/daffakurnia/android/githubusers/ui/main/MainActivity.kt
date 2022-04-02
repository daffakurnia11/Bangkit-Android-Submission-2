package me.daffakurnia.android.githubusers.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import me.daffakurnia.android.githubusers.R
import me.daffakurnia.android.githubusers.api.ApiConfig
import me.daffakurnia.android.githubusers.dataclass.DataUser
import me.daffakurnia.android.githubusers.response.ItemsItem
import me.daffakurnia.android.githubusers.response.UserSearchResponse
import me.daffakurnia.android.githubusers.databinding.ActivityMainBinding
import me.daffakurnia.android.githubusers.ui.favorite.FavoriteActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val layoutManager = GridLayoutManager(this, 2)
            binding.listUsers.layoutManager = layoutManager
        } else {
            val layoutManager = LinearLayoutManager(this)
            binding.listUsers.layoutManager = layoutManager
        }

        findUsers()
    }

    private fun findUsers() {
        showProgressbar(true)
        val client = ApiConfig.getApiService().getAccount(USERNAME)
        client.enqueue(object : Callback<UserSearchResponse> {
            override fun onResponse(
                call: Call<UserSearchResponse>,
                response: Response<UserSearchResponse>
            ) {
                if (response.isSuccessful) {
                    showProgressbar(false)
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

    private fun showProgressbar(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite_menu -> {
                startActivity(Intent(this, FavoriteActivity::class.java))
                true
            }
            else -> true
        }
    }

    companion object {
        private var USERNAME = "username"
    }
}