package com.github.coutinhonobre.data.features.login.repository

import com.github.coutinhonobre.domain.feature.login.repository.LoginRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FirebaseLoginRepositoryImplTest {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var loginRepository: LoginRepository

    @BeforeEach
    fun setUp() {
        firebaseAuth = mockk()
        loginRepository =
            com.github.coutinhonobre.data.features.login.repository.FirebaseLoginRepositoryImpl(
                firebaseAuth
            )
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `login should return success when authentication is successful`() = runTest {
        val email = "test@example.com"
        val password = "password"
        val authResult = mockk<AuthResult>()
        val task = mockk<com.google.android.gms.tasks.Task<AuthResult>>()

        every { task.isSuccessful } returns true
        every { task.result } returns authResult
        every { firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(any()) } answers {
            firstArg<com.google.android.gms.tasks.OnCompleteListener<AuthResult>>().onComplete(task)
            task
        }

        val result = loginRepository.login(email, password)

        assertTrue(result.isSuccess)
        assertEquals(true, result.getOrNull())
    }

    @Test
    fun `login should return failure when authentication fails`() = runTest {
        val email = "test@example.com"
        val password = "password"
        val task = mockk<com.google.android.gms.tasks.Task<AuthResult>>()
        val exception = Exception("Authentication failed")

        every { task.isSuccessful } returns false
        every { task.exception } returns exception
        every { firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(any()) } answers {
            firstArg<com.google.android.gms.tasks.OnCompleteListener<AuthResult>>().onComplete(task)
            task
        }

        val result = loginRepository.login(email, password)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}