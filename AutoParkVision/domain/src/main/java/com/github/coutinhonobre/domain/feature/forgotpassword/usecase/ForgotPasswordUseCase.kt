package com.github.coutinhonobre.domain.feature.forgotpassword.usecase

interface ForgotPasswordUseCase {
    suspend fun forgotPassword(email: String): Result<Boolean>
}