package com.example.coincapapp.viewModels


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coincapapp.authentication.AuthClient
import com.example.coincapapp.authentication.AuthError
import com.example.coincapapp.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authClient: AuthClient
) : ViewModel() {

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    var showError by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf("")
        private set

    var user by mutableStateOf<User?>(null)
        private set

    init {
        viewModelScope.launch {
            try {
                user = authClient.getCurrentUser()
            } catch (e: AuthError) {
                // Handle the error if needed, maybe set showError and errorMessage
                println("Error getting current user: ${e.message}")
            }
        }
    }

    fun updateEmail(newEmail: String) {
        email = newEmail
    }

    fun updatePassword(newPassword: String) {
        password = newPassword
    }

    fun login() {
        viewModelScope.launch {
            try {
                user = authClient.signIn(email, password)
                email = ""
                password = ""
                showError = false
                errorMessage = ""
            } catch (e: AuthError) {
                showError = true
                errorMessage = e.message ?: "Login failed"
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                authClient.signOut()
                user = null
                showError = false
                errorMessage = ""
            } catch (e: AuthError) {
                showError = true
                errorMessage = e.message ?: "Logout failed"
            }
        }
    }


}