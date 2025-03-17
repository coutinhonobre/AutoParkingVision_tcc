package com.github.coutinhonobre.domain.feature.login.usecase

interface LoginUseCase {
    suspend fun login(email: String, password: String) : Result<Boolean>
}