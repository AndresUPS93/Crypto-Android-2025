package com.example.coincapapp.authentication


import com.example.coincapapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject


interface AuthClient {
    suspend fun signIn(email: String, password: String): User
    suspend fun signOut()
    suspend fun getCurrentUser(): User
}

sealed class AuthError(override val message: String? = null) : Exception() {
    data class SignInFailed(val errorMessage: String) : AuthError(errorMessage)
    object UserNotFound : AuthError("User not found") {
        private fun readResolve(): Any = UserNotFound
    }

    object SignOutFailed : AuthError("Sign out failed") {
        private fun readResolve(): Any = SignOutFailed
    }
}

class FirebaseAuthClient @Inject constructor() : AuthClient {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun signIn(email: String, password: String): User {
        return try {
            val result: AuthResult = auth.signInWithEmailAndPassword(email, password).await()
            User(result.user?.uid ?: "", result.user?.email ?: "")
        } catch (e: Exception) {
            throw AuthError.SignInFailed(e.localizedMessage ?: "Sign in failed")
        }
    }

    override suspend fun signOut() {
        return try {
            auth.signOut()
        } catch (e: Exception) {
            throw AuthError.SignOutFailed
        }
    }

    override suspend fun getCurrentUser(): User {
        val currentUser = auth.currentUser
        return currentUser?.let {
            User(it.uid, it.email ?: "")
        } ?: throw AuthError.UserNotFound
    }
}


