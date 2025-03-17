package com.github.coutinhonobre.domain.feature.forgotpassword.repository

interface ForgotPasswordRepository {
    suspend fun forgotPassword(email: String): Result<Boolean>
}