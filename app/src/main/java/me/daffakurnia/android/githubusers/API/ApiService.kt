package me.daffakurnia.android.githubusers.API

import me.daffakurnia.android.githubusers.Response.UserDetailResponse
import me.daffakurnia.android.githubusers.Response.UserFollowersResponse
import me.daffakurnia.android.githubusers.Response.UserFollowingResponse
import me.daffakurnia.android.githubusers.Response.UserSearchResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: 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")
    fun getAccount(
        @Query("q") id: String
    ): Call<UserSearchResponse>

    @GET("users/{username}")
    @Headers("Authorization: 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")
    fun getDetail(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: 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")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<UserFollowersResponse>>

    @GET("users/{username}/following")
    @Headers("Authorization: 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")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<UserFollowingResponse>>
}