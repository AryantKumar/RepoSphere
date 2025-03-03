package com.aryant.github.search

import com.aryant.github.data.GitHubApiService
import com.aryant.github.data.Repository
import com.aryant.github.search.SearchResponse
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val apiService: GitHubApiService
) {
    suspend fun searchRepositories(query: String): List<Repository>? {
        return try {
            if (query.isBlank()) return emptyList() // ✅ Avoid empty queries causing API failures

            val response: SearchResponse = apiService.searchRepositories(query)
            response.items ?: emptyList()
        } catch (e: Exception) {
            null
        }
    }
}
