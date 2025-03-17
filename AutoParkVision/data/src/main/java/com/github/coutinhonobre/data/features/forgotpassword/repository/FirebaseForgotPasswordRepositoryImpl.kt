package com.github.coutinhonobre.data.features.forgotpassword.repository

import com.github.coutinhonobre.domain.feature.forgotpassword.repository.ForgotPasswordRepository
import com.google.firebase.auth.FirebaseAuth
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseForgotPasswordRepositoryImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ForgotPasswordRepository {
    override suspend fun forgotPassword(email: String): Result<Boolean> {
        return suspendCoroutine { continuation ->
            auth.sendPasswordResetEmail(email)
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