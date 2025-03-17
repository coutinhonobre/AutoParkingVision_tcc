package com.github.coutinhonobre.domain.feature.login.repository

interface LoginRepository {
    suspend fun login(email: String, password: String): Result<Boolean>
}