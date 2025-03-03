package com.aryant.github.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.aryant.github.R
import com.aryant.github.main.MainActivity
import com.aryant.github.utils.TokenManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import net.openid.appauth.*

class AuthActivity : AppCompatActivity() {

    private lateinit var authService: AuthorizationService
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var tokenManager: TokenManager

    private lateinit var btnGitHubSignIn: Button
    private lateinit var btnGoogleSignIn: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var textViewLoading: TextView

    // 🔹 Enter Your GitHub Client ID & Secret Here
    private val clientId = "Ov23liMZsRkOs7R6RSoS" // ✅ REPLACE WITH YOUR CLIENT ID
    private val clientSecret = "dc9a86785610f791a841c6b6f8df258ac3f4e2f2" // ✅ REPLACE WITH YOUR CLIENT SECRET
    private val redirectUri = Uri.parse("com.aryant.github://callback")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        tokenManager = TokenManager(this)
        authService = AuthorizationService(this)
        auth = FirebaseAuth.getInstance()

        btnGitHubSignIn = findViewById(R.id.btnGitHubSignIn)
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn)
        progressBar = findViewById(R.id.progressBar)
        textViewLoading = findViewById(R.id.textViewLoading)

        // 🔹 Google Sign-In Configuration
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // 🔹 If already signed in, go to MainActivity
        val token = tokenManager.getGitHubToken()
        if (!token.isNullOrEmpty() || auth.currentUser != null) {
            Log.d(TAG, "✅ User already signed in. Redirecting to MainActivity.")
            navigateToMain()
            return
        }

        // 🔹 GitHub Sign-In Button Click
        btnGitHubSignIn.setOnClickListener {
            startGitHubOAuth()
        }

        // 🔹 Google Sign-In Button Click
        btnGoogleSignIn.setOnClickListener {
            signInWithGoogle()
        }
    }

    // 🔹 Start GitHub OAuth Flow
    private fun startGitHubOAuth() {
        progressBar.visibility = ProgressBar.VISIBLE
        textViewLoading.text = "Redirecting to GitHub for authentication..."
        btnGitHubSignIn.isEnabled = false

        val serviceConfig = AuthorizationServiceConfiguration(
            Uri.parse("https://github.com/login/oauth/authorize"),
            Uri.parse("https://github.com/login/oauth/access_token")
        )

        val authRequest = AuthorizationRequest.Builder(
            serviceConfig,
            clientId,
            ResponseTypeValues.CODE,
            redirectUri
        ).setScopes("repo", "user")
            .build()

        val authIntent = authService.getAuthorizationRequestIntent(authRequest)
        authLauncher.launch(authIntent)
    }

    // 🔹 Handle GitHub OAuth Result
    private val authLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data ?: return@registerForActivityResult
            val response = AuthorizationResponse.fromIntent(data)
            val ex = AuthorizationException.fromIntent(data)

            if (response != null) {
                exchangeCodeForToken(response)
            } else {
                Log.e(TAG, "❌ GitHub OAuth failed: ${ex?.message}")
                showToast("GitHub Authentication Failed")
                progressBar.visibility = ProgressBar.GONE
                btnGitHubSignIn.isEnabled = true
            }
        }

    // 🔹 Exchange Authorization Code for Access Token
    private fun exchangeCodeForToken(response: AuthorizationResponse) {
        progressBar.visibility = ProgressBar.VISIBLE
        textViewLoading.text = "Fetching GitHub access token..."

        val tokenRequest = response.createTokenExchangeRequest()
        val clientAuthentication = ClientSecretPost(clientSecret) // ✅ Correct way to send Client Secret

        authService.performTokenRequest(tokenRequest, clientAuthentication) { tokenResponse, ex ->
            if (tokenResponse != null) {
                val accessToken = tokenResponse.accessToken
                if (!accessToken.isNullOrEmpty()) {
                    Log.d(TAG, "✅ GitHub Token Received: $accessToken")

                    // 🔹 Save Token Securely
                    tokenManager.saveGitHubToken(accessToken)

                    // 🔹 Navigate to MainActivity
                    navigateToMain()
                } else {
                    Log.e(TAG, "❌ Received empty GitHub token!")
                    showToast("GitHub Authentication Failed")
                    progressBar.visibility = ProgressBar.GONE
                    btnGitHubSignIn.isEnabled = true
                }
            } else {
                Log.e(TAG, "❌ Token Exchange Failed: ${ex?.message}")
                showToast("GitHub Authentication Failed")
                progressBar.visibility = ProgressBar.GONE
                btnGitHubSignIn.isEnabled = true
            }
        }
    }

    // 🔹 Google Sign-In
    private fun signInWithGoogle() {
        Log.d(TAG, "🚀 Starting Google Sign-In process...")
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "✅ Google Sign-In result received")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    Log.d(TAG, "✅ Google Sign-In successful: ${account.email}")
                    firebaseAuthWithGoogle(account.idToken!!)
                } else {
                    Log.e(TAG, "❌ Google Sign-In failed: Account is null")
                    showToast("Google Sign-In failed. Please try again.")
                }
            } catch (e: ApiException) {
                Log.e(TAG, "❌ Google Sign-In Error: ${e.statusCode} - ${e.localizedMessage}", e)
                showToast("Google Sign-In failed. Error code: ${e.statusCode}")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        Log.d(TAG, "🚀 Authenticating with Firebase using Google token...")
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                        if (tokenTask.isSuccessful) {
                            val firebaseToken = tokenTask.result?.token
                            if (!firebaseToken.isNullOrEmpty()) {
                                Log.d(TAG, "✅ Firebase Token: $firebaseToken")

                                // 🔹 Store Firebase Token in TokenManager
                                tokenManager.saveGitHubToken(firebaseToken)

                                // 🔹 Navigate to MainActivity
                                navigateToMain()
                            }
                        } else {
                            Log.e(TAG, "❌ Failed to fetch Firebase token: ${tokenTask.exception?.message}")
                            showToast("Authentication Failed")
                        }
                    }
                } else {
                    Log.e(TAG, "❌ Firebase Auth Error: ${task.exception?.localizedMessage}")
                    showToast("Authentication Failed: ${task.exception?.localizedMessage}")
                }
            }
    }


    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        authService.dispose()
    }

    companion object {
        private const val TAG = "AuthActivity"
        private const val RC_SIGN_IN = 9001
    }
}
