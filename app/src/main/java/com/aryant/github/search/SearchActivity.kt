package com.aryant.github.search

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aryant.github.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var searchEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchEditText = findViewById(R.id.searchEditText)
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        searchAdapter = SearchAdapter(emptyList())
        recyclerView.adapter = searchAdapter

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = searchEditText.text.toString().trim()
                if (query.isNotEmpty()) {
                    searchRepositories(query) // ✅ Fix: Removed token
                }
                true
            } else {
                false
            }
        }
    }

    private fun searchRepositories(query: String) {
        progressBar.visibility = android.view.View.VISIBLE
        searchViewModel.repositories.removeObservers(this)

        searchViewModel.searchRepositories(query) // ✅ Fix: Removed token
        searchViewModel.repositories.observe(this) { repos ->
            progressBar.visibility = android.view.View.GONE
            if (repos != null) {
                searchAdapter.updateData(repos)
            } else {
                Toast.makeText(this, "Failed to fetch repositories", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
