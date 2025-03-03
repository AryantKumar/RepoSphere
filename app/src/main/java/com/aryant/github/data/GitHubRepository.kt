package com.aryant.github.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GitHubRepository {

    private val api: GitHubApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(GitHubApiService::class.java)
    }

    suspend fun getUserRepositories(token: String): List<Repository> {
        return api.getUserRepositories("Bearer $token")
    }

    suspend fun searchRepositories(query: String): List<Repository> { // ✅ Fix: Removed token parameter
        return api.searchRepositories(query).items.orEmpty() // ✅ Fix: Prevent null issues
    }
}
