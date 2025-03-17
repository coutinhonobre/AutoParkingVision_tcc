package com.github.coutinhonobre.data.features.login.usecase

import com.github.coutinhonobre.domain.feature.login.repository.LoginRepository
import com.github.coutinhonobre.domain.feature.login.usecase.LoginUseCaseImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LoginUseCaseImplTest {

    private lateinit var useCase: LoginUseCaseImpl
    private lateinit var repository: LoginRepository

    @BeforeEach
    fun setup() {
        repository = mockk()
        useCase = LoginUseCaseImpl(repository)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should call repository login`() = runTest {
        // Given
        val email = "email"
        val password = "password"
        val expectedResult = Result.success(true)
        coEvery { repository.login(email, password) } returns expectedResult

        // When
        val result = useCase.login(email, password)

        // Then
        coVerify { repository.login(email, password) }
        assert(result == expectedResult)
    }

    @Test
    fun `should return error when repository login throws exception`() = runTest {
        // Given
        val email = "email"
        val password = "password"
        coEvery { repository.login(email, password) } returns Result.failure(Exception())

        // When
        val result = useCase.login(email, password)

        // Then
        coVerify { repository.login(email, password) }
        assert(result.isFailure)
    }
}
