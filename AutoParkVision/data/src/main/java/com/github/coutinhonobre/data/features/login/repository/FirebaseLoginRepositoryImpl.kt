package com.github.coutinhonobre.data.features.login.repository

import com.github.coutinhonobre.domain.feature.login.repository.LoginRepository
import com.google.firebase.auth.FirebaseAuth
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseLoginRepositoryImpl(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) : LoginRepository {
    override suspend fun login(email: String, password: String): Result<Boolean> {
        return suspendCoroutine { continuation ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.success(true))
                    } else {
                        continuation.resume(Result.failure(task.exception ?: Exception("Unknown error")))
                    }
                }
        }
    }
}
