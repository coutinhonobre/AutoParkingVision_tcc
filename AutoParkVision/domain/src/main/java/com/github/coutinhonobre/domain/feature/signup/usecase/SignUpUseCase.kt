package com.github.coutinhonobre.domain.feature.signup.usecase

interface SignUpUseCase {
    suspend fun execute(
        email: String,
        password: String,
        confirmPassword: String
    ): Result<Boolean>
}
