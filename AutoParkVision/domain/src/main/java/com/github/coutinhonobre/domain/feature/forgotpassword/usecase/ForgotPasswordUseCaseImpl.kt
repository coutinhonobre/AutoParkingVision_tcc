package com.github.coutinhonobre.domain.feature.forgotpassword.usecase

import com.github.coutinhonobre.domain.feature.forgotpassword.repository.ForgotPasswordRepository

class ForgotPasswordUseCaseImpl(
    private val repository: ForgotPasswordRepository
): ForgotPasswordUseCase {
    override suspend fun forgotPassword(email: String): Result<Boolean> {
        return repository.forgotPassword(email)
    }
}