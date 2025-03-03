package com.aryant.github.data

import com.aryant.github.search.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GitHubApiService {
    @GET("user/repos")
    suspend fun getUserRepositories(
        @Header("Authorization") token: String
    ): List<Repository>

    @GET("user/starred")
    suspend fun getUserStarredRepositories(
        @Header("Authorization") token: String
    ): List<Repository>

    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String // ✅ Ensure query parameter is correct
    ): SearchResponse
}

