package com.aryant.github.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryant.github.data.GitHubRepository
import com.aryant.github.data.Repository
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData

class MainViewModel : ViewModel() {

    private val repository = GitHubRepository()

    private val _repos = MutableLiveData<List<Repository>>()
    val repos: LiveData<List<Repository>> get() = _repos

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private var fullRepoList: List<Repository> = emptyList() // Store all fetched repositories

    fun fetchRepositories(token: String) {
        viewModelScope.launch {
            try {
                val repoList = repository.getUserRepositories(token)
                fullRepoList = repoList // Store full list for filtering
                _repos.value = repoList
            } catch (e: Exception) {
                _error.value = "Failed to fetch repositories: ${e.localizedMessage}"
            }
        }
    }

    // 🔹 Function to filter repositories based on search input
    fun filterRepositories(query: String) {
        val filteredList = fullRepoList.filter { it.name.contains(query, ignoreCase = true) }
        _repos.value = filteredList
    }
}
