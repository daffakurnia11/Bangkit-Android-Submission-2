package me.daffakurnia.android.githubusers.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import me.daffakurnia.android.githubusers.R
import me.daffakurnia.android.githubusers.api.ApiConfig
import me.daffakurnia.android.githubusers.database.Favorite
import me.daffakurnia.android.githubusers.response.UserDetailResponse
import me.daffakurnia.android.githubusers.databinding.ActivityDetailBinding
import me.daffakurnia.android.githubusers.helper.DateHelper
import me.daffakurnia.android.githubusers.helper.ViewModelFactory
import me.daffakurnia.android.githubusers.ui.favorite.FavoriteViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var favorite: Favorite
    private lateinit var favoriteViewModel: FavoriteViewModel

    private var isExist = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        favoriteViewModel = obtainViewModel(this@DetailActivity)

        favoriteViewModel.getFavorite(intent.getStringExtra(USERNAME)!!).observe(this) { userList ->
            if (userList != null) {
                isExist = true
                favorite = userList
                binding.favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_24)
            } else {
                favorite = Favorite()
                binding.favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }
        }

        val userDetail = intent.getStringExtra(USERNAME)

        getDetailUser(userDetail)

        val bundle = Bundle()
        bundle.putString(USERNAME, userDetail)

        val sectionPagerAdapter = SectionsPagerAdapter(this, bundle)
        binding.viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.title = resources.getString(R.string.title_detail_activity)
        supportActionBar?.elevation = 0f

        binding.favoriteButton.setOnClickListener {
            val login = intent.getStringExtra(USERNAME)
            val url = intent.getStringExtra(URL)
            val avatarUrl = intent.getStringExtra(AVATAR)
            favorite.let { favorite ->
                favorite?.login = login
                favorite?.url = url
                favorite?.avatar_url = avatarUrl
                favorite?.date = DateHelper.getCurrentDate()
            }
            if (isExist) {
                favoriteViewModel.delete(favorite as Favorite)
                binding.favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                Toast.makeText(this, "Data deleted", Toast.LENGTH_SHORT).show()
            } else {
                favoriteViewModel.insert(favorite as Favorite)
                binding.favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_24)
                Toast.makeText(this, "Data added", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    private fun getDetailUser(userDetail: String?) {
        binding.progressBar.visibility = View.VISIBLE
        val client = ApiConfig.getApiService().getDetail(userDetail.toString())
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                if (response.isSuccessful) {
                    binding.progressBar.visibility = View.INVISIBLE
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Glide.with(this@DetailActivity)
                            .load(responseBody.avatarUrl)
                            .circleCrop()
                            .into(binding.imgDetailUser)
                        binding.apply {
                            textDetailUsername.text = responseBody.login
                            textDetailName.text = responseBody.name
                            textDetailLocation.text = (responseBody.location
                                ?: resources.getString(R.string.default_location)).toString()
                            textDetailCompany.text = (responseBody.company
                                ?: resources.getString(R.string.default_company)).toString()
                            textNumberFollowers.text = responseBody.followers.toString()
                            textNumberFollowing.text = responseBody.following.toString()
                            textNumberRepository.text = responseBody.publicRepos.toString()
                        }
                    }
                } else {
                    Log.e(this@DetailActivity.toString(), "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                Log.e(this@DetailActivity.toString(), "onFailure: ${t.message}")
            }

        })
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_followers,
            R.string.tab_following
        )
        const val USERNAME = "username"
        const val URL = "url"
        const val AVATAR = "avatar"
    }
}