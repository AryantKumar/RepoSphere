package com.aryant.github.utils

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)

    fun saveGitHubToken(token: String) {
        sharedPreferences.edit().putString("GITHUB_TOKEN", token).apply()
    }

    fun getGitHubToken(): String? {
        return sharedPreferences.getString("GITHUB_TOKEN", null)
    }

    fun saveGoogleSignInStatus(isSignedIn: Boolean) {
        sharedPreferences.edit().putBoolean("GOOGLE_SIGNED_IN", isSignedIn).apply()
    }

    fun isGoogleSignedIn(): Boolean {
        return sharedPreferences.getBoolean("GOOGLE_SIGNED_IN", false)
    }

    fun clearAuthData() {
        sharedPreferences.edit().clear().apply()
    }
}
