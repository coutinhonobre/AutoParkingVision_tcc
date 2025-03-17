package com.github.coutinhonobre.data.features.signup.repository

import com.github.coutinhonobre.domain.feature.signup.exception.SignUpAuthUserCollisionException
import com.github.coutinhonobre.domain.feature.signup.exception.SignUpGenericException
import com.github.coutinhonobre.domain.feature.signup.repository.SignUpRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseSignUpRepositoryImpl(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) : SignUpRepository {
    override suspend fun signUp(email: String, password: String): Result<Boolean> {
        return suspendCoroutine {  continuation ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.success(true))
                    } else {
                        continuation.resume(Result.failure(task.exception ?: Exception("Unknown error")))
                        val exception = task.exception
                        when (exception) {
                            is FirebaseAuthUserCollisionException -> {
                                continuation.resume(Result.failure(SignUpAuthUserCollisionException()))
                            }
                            else -> {
                                continuation.resume(Result.failure(SignUpGenericException()))
                            }
                        }
                    }
                }
        }
    }
}