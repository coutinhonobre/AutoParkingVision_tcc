package com.github.coutinhonobre.data.features.signup.repository

import com.github.coutinhonobre.domain.feature.signup.repository.SignUpRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FirebaseSignUpRepositoryImplTest {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var signUpRepository: SignUpRepository

    @BeforeEach
    fun setUp() {
        firebaseAuth = mockk()
        signUpRepository =
            FirebaseSignUpRepositoryImpl(
                firebaseAuth
            )
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `signUp should return success when create user is successful`() = runTest {
        // Given
        val email = "teste@teste.com"
        val password = "123456"
        val authResult = mockk<AuthResult>()
        val task = mockk<com.google.android.gms.tasks.Task<AuthResult>>()

        // When
        every { task.isSuccessful } returns true
        every { task.result } returns authResult
        every { firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(any()) } answers {
            firstArg<com.google.android.gms.tasks.OnCompleteListener<AuthResult>>().onComplete(task)
            task
        }

        // Then
        val result = signUpRepository.signUp(email, password)

        assertTrue(result.isSuccess)
        assertEquals(true, result.getOrNull())
    }

    @Test
    fun `signUp should return failure when create user fails`() = runTest {
        // Given
        val email = "teste@teste.com"
        val password = "123456"
        val task = mockk<com.google.android.gms.tasks.Task<AuthResult>>()
        val exception = Exception("Create user failed")

        // When
        every { task.isSuccessful } returns false
        every { task.exception } returns exception
        every { firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(any()) } answers {
            firstArg<com.google.android.gms.tasks.OnCompleteListener<AuthResult>>().onComplete(task)
            task
        }

        // Then
        val result = signUpRepository.signUp(email, password)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
