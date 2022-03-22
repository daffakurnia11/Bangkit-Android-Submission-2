package me.daffakurnia.android.githubusers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import me.daffakurnia.android.githubusers.databinding.ActivityDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userDetail = intent.getStringExtra(USERNAME)

        getDetailUser(userDetail)

        val sectionPagerAdapter = SectionsPagerAdapter(this)
        binding.viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.title = "Detail User"
        supportActionBar?.elevation = 0f
    }

    private fun getDetailUser(userDetail: String?) {
        val client = ApiConfig.getApiService().getDetail(userDetail.toString())
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Glide.with(this@DetailActivity)
                            .load(responseBody.avatarUrl)
                            .circleCrop()
                            .into(binding.imgDetailUser)
                        binding.textDetailUsername.text = responseBody.login
                        binding.textDetailName.text = responseBody.name
                        binding.textNumberFollowers.text = responseBody.followers.toString()
                        binding.textNumberFollowing.text = responseBody.following.toString()
                        binding.textNumberRepository.text = responseBody.publicRepos.toString()
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
    }
}