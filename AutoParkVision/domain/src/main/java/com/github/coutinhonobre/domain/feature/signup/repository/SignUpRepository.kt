package com.github.coutinhonobre.domain.feature.signup.repository

interface SignUpRepository {
    suspend fun signUp(email: String, password: String): Result<Boolean>
}