package me.daffakurnia.android.githubusers.ui.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import me.daffakurnia.android.githubusers.R
import me.daffakurnia.android.githubusers.databinding.ActivityFavoriteBinding
import me.daffakurnia.android.githubusers.helper.ViewModelFactory
import me.daffakurnia.android.githubusers.ui.main.ListUserAdapter

class FavoriteActivity : AppCompatActivity() {

    private var _binding: ActivityFavoriteBinding? = null
    private val binding get() = _binding
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val favoriteViewModel = obtainViewModel(this@FavoriteActivity)
        favoriteViewModel.getAllFavorite().observe(this) { userList ->
            if (userList != null) {
                adapter.setListUsers(userList)
            }
        }

        adapter = FavoriteAdapter()
        binding?.listFavorites?.layoutManager = LinearLayoutManager(this)
        binding?.listFavorites?.setHasFixedSize(true)
        binding?.listFavorites?.adapter = adapter
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}