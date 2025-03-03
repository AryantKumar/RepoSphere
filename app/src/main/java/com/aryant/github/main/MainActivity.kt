package com.aryant.github.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aryant.github.R
import com.aryant.github.auth.AuthActivity
import com.aryant.github.data.GitHubApiService
import com.aryant.github.data.Repository
import com.aryant.github.utils.TokenManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var tokenManager: TokenManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var repoAdapter: RepoAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var textViewWelcome: TextView
    private lateinit var searchEditText: EditText
    private lateinit var githubApiService: GitHubApiService
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private var fullRepoList: List<Repository> = emptyList() // Store full repo list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tokenManager = TokenManager(this)

        // 🔹 Initialize UI Components
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        textViewWelcome = findViewById(R.id.textViewWelcome)
        searchEditText = findViewById(R.id.searchEditText)

        recyclerView.layoutManager = LinearLayoutManager(this)
        repoAdapter = RepoAdapter(emptyList())
        recyclerView.adapter = repoAdapter

        // 🔹 Setup Retrofit API Service with Logging
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        githubApiService = retrofit.create(GitHubApiService::class.java)

        // 🔹 Check if user is logged in with GitHub or Google
        val githubToken = tokenManager.getGitHubToken()
        val firebaseUser = firebaseAuth.currentUser

        when {
            !githubToken.isNullOrEmpty() -> {
                Log.d(TAG, "✅ GitHub User Detected - Fetching Repositories")
                fetchRepositories(githubToken)
            }
            firebaseUser != null -> {
                Log.d(TAG, "✅ Google User Detected - Fetching Public Repositories")
                fetchPublicRepositories()
            }
            else -> {
                Log.e(TAG, "❌ No Auth Token Found! Redirecting to AuthActivity.")
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
        }

        /* 🔹 Listen for search queries */
        searchEditText.addTextChangedListener { text ->
            filterRepositories(text.toString()) // ✅ Call local function
        }


    }

    // 🔹 Fetch Private Repositories (If Signed in with GitHub)
    private fun fetchRepositories(token: String) {
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                Log.d(TAG, "🚀 Fetching Private Repositories with Token: token $token")
                val repoList = githubApiService.getUserRepositories("token $token")

                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    if (repoList.isNotEmpty()) {
                        Log.d(TAG, "✅ Private Repositories Fetched: ${repoList.size}")
                        fullRepoList = repoList // Store full list
                        repoAdapter.updateList(repoList)
                    } else {
                        Log.e(TAG, "❌ No Private Repositories Found")
                        textViewWelcome.text = "No repositories available."
                    }
                }
            } catch (e: Exception) {
                handleApiError(e)
            }
        }
    }

    // 🔹 Fetch Public Repositories (If Signed in with Google)
    private fun fetchPublicRepositories() {
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                Log.d(TAG, "🚀 Fetching Public Repositories")
                val repoList = githubApiService.getUserRepositories("") // ✅ Public API (No Token Needed)

                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    if (repoList.isNotEmpty()) {
                        Log.d(TAG, "✅ Public Repositories Fetched: ${repoList.size}")
                        fullRepoList = repoList // Store full list
                        repoAdapter.updateList(repoList)
                    } else {
                        Log.e(TAG, "❌ No Public Repositories Found")
                        textViewWelcome.text = "No public repositories available."
                    }
                }
            } catch (e: Exception) {
                handleApiError(e)
            }
        }
    }

    // 🔹 Filter repositories based on search query
    private fun filterRepositories(query: String) {
        val filteredList = fullRepoList.filter { it.name.contains(query, ignoreCase = true) }
        repoAdapter.updateList(filteredList)
    }

    private fun handleApiError(e: Exception) {
        lifecycleScope.launch(Dispatchers.Main) {
            progressBar.visibility = View.GONE
            textViewWelcome.text = "Error fetching repositories."
            Log.e(TAG, "❌ API Error: ${e.message}")
            Toast.makeText(this@MainActivity, "Failed to fetch repositories", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
