package me.daffakurnia.android.githubusers

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
}