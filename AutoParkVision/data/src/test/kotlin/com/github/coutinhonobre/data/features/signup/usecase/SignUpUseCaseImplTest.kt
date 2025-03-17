package com.github.coutinhonobre.data.features.signup.usecase

import com.github.coutinhonobre.domain.feature.signup.exception.SignUpPasswordsDoNotMatchException
import com.github.coutinhonobre.domain.feature.signup.repository.SignUpRepository
import com.github.coutinhonobre.domain.feature.signup.usecase.SignUpUseCase
import com.github.coutinhonobre.domain.feature.signup.usecase.SignUpUseCaseImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SignUpUseCaseImplTest {

    private lateinit var useCase: SignUpUseCase
    private lateinit var repository: SignUpRepository

    @BeforeEach
    fun setup() {
        repository = mockk()
        useCase = SignUpUseCaseImpl(repository)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should call repository signUp`() = runTest {
        // Given
        val email = "email"
        val password = "password"
        val confirmPassword = "password"
        val expectedResult = Result.success(true)
        coEvery {
            repository.signUp(email, password)
        } returns expectedResult

        // When
        val result = useCase.execute(
            email = email,
            password = password,
            confirmPassword = confirmPassword
        )

        // Then
        coVerify { repository.signUp(email, password) }
        assert(result == expectedResult)
    }

    @Test
    fun `should return error when repository signUp throws exception`() = runTest {
        // Given
        val email = "email"
        val password = "password"
        val confirmPassword = "password"

        coEvery { repository.signUp(email, password) } returns Result.failure(Exception())

        // When
        val result = useCase.execute(
            email = email,
            password = password,
            confirmPassword = confirmPassword
        )

        // Then
        coVerify { repository.signUp(email, password) }
        assert(result.isFailure)
    }

    // senhas diferentes
    @Test
    fun `should return error when password and confirmPassword are different`() = runTest {
        // Given
        val email = "email"
        val password = "password"
        val confirmPassword = "password2"

        // When
        val result = useCase.execute(
            email = email,
            password = password,
            confirmPassword = confirmPassword
        )

        // Then
        assert(result.isFailure)
        result.onFailure { assert(it is SignUpPasswordsDoNotMatchException) }
    }
}