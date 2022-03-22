package me.daffakurnia.android.githubusers

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import me.daffakurnia.android.githubusers.API.ApiConfig
import me.daffakurnia.android.githubusers.DataClass.DataUser
import me.daffakurnia.android.githubusers.Response.UserFollowingResponse
import me.daffakurnia.android.githubusers.databinding.FragmentFollowingBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(FollowingFragment.USERNAME).toString()

        binding = FragmentFollowingBinding.bind(view)

        getFollowing(username)
    }

    private fun getFollowing(username: String) {
        showProgressbar(true)
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<UserFollowingResponse>> {
            override fun onResponse(
                call: Call<List<UserFollowingResponse>>,
                response: Response<List<UserFollowingResponse>>
            ) {
                if (response.isSuccessful) {
                    showProgressbar(false)
                    val responseBody = response.body()
                    if (responseBody != null) {
                        getFollowingData(responseBody)
                    }
                }
            }

            override fun onFailure(call: Call<List<UserFollowingResponse>>, t: Throwable) {
                Log.d("Failure", t.message, t)
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

    private fun getFollowingData(responseBody: List<UserFollowingResponse>) {
        val userList = ArrayList<DataUser>()

        for (item in responseBody) {
            userList.add(
                DataUser(
                    item.login,
                    item.htmlUrl,
                    item.avatarUrl
                )
            )
        }
        val adapter = ListUserAdapter(userList)

        binding.apply {
            listFollowing.setHasFixedSize(true)
            if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                val layoutManager = GridLayoutManager(activity, 2)
                listFollowing.layoutManager = layoutManager
            } else {
                val layoutManager = LinearLayoutManager(activity)
                listFollowing.layoutManager = layoutManager
            }
            listFollowing.adapter = adapter
        }
    }

    companion object {
        private var USERNAME = "username"
    }
}