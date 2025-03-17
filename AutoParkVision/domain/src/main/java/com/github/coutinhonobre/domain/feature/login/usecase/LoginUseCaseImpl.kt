package com.github.coutinhonobre.domain.feature.login.usecase

import com.github.coutinhonobre.domain.feature.login.repository.LoginRepository

class LoginUseCaseImpl(
    private val repository: LoginRepository
): LoginUseCase {
    override suspend fun login(email: String, password: String): Result<Boolean> {
        return repository.login(email, password)
    }
}