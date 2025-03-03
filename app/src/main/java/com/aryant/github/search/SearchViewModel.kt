package com.aryant.github.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryant.github.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _repositories = MutableLiveData<List<Repository>?>()
    val repositories: LiveData<List<Repository>?> = _repositories

    fun searchRepositories(query: String) {
        if (query.isBlank()) return // ✅ Prevent empty query calls

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = searchRepository.searchRepositories(query)
                _repositories.postValue(result ?: emptyList())
            } catch (e: Exception) {
                _repositories.postValue(null)
            }
        }
    }
}
