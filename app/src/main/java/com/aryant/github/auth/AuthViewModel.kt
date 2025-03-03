package com.aryant.github.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>(AuthState.Loading) // 🔹 Explicit Initialization
    val authState: LiveData<AuthState> get() = _authState

    // 🔹 Update Auth State
    fun updateAuthState(state: AuthState) {
        _authState.postValue(state) // 🔹 Ensures thread safety
    }

    // 🔹 Sign in with Google
    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    _authState.postValue(AuthState.Success(user?.uid ?: ""))
                } else {
                    _authState.postValue(AuthState.Error(task.exception?.localizedMessage ?: "Google Authentication Failed"))
                }
            }
    }

    // 🔹 Sign out
    fun logout() {
        firebaseAuth.signOut()
        _authState.postValue(AuthState.Loading)
    }

    sealed class AuthState {
        object Loading : AuthState()
        data class Success(val token: String) : AuthState() // ✅ Can hold GitHub or Google Token
        data class Error(val message: String) : AuthState()
    }
}
