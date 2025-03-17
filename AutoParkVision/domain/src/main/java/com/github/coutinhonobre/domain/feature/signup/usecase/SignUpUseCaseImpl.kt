package com.github.coutinhonobre.domain.feature.signup.usecase

import com.github.coutinhonobre.domain.feature.signup.exception.SignUpAuthUserCollisionException
import com.github.coutinhonobre.domain.feature.signup.exception.SignUpGenericException
import com.github.coutinhonobre.domain.feature.signup.exception.SignUpPasswordsDoNotMatchException
import com.github.coutinhonobre.domain.feature.signup.repository.SignUpRepository

class SignUpUseCaseImpl(
    private val repository: SignUpRepository
) : SignUpUseCase {
    override suspend fun execute(
        email: String,
        password: String,
        confirmPassword: String
    ): Result<Boolean> {
        if (password != confirmPassword) {
            return Result.failure(SignUpPasswordsDoNotMatchException())
        }
        return repository.signUp(
            email = email,
            password = password
        ).fold(
            onSuccess = {
                Result.success(true)
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }
}