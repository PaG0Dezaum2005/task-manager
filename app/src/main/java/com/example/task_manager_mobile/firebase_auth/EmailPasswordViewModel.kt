package com.example.task_manager_mobile.firebase_auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class EmailPasswordViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow<AuthResultState>(AuthResultState.Idle)
    val authState = _authState.asStateFlow()

    fun signIn(email: String, password: String) {
        _authState.value = AuthResultState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _authState.value = if (task.isSuccessful) {
                    AuthResultState.Success
                } else {
                    AuthResultState.Failure(task.exception?.message ?: "Erro")
                }
            }
    }

    sealed class AuthResultState {
        object Idle : AuthResultState()
        object Loading : AuthResultState()
        object Success : AuthResultState()
        data class Failure(val error: String) : AuthResultState()
    }

    fun register(email: String, password: String) {
        _authState.value = AuthResultState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _authState.value = if (task.isSuccessful) {
                    AuthResultState.Success
                } else {
                    AuthResultState.Failure(task.exception?.message ?: "Erro")
                }
            }
    }

    fun resetAuthState() {
        _authState.value = AuthResultState.Idle
    }
}
