package com.github.coutinhonobre.data.features.forgotpassword.repository

import com.github.coutinhonobre.domain.feature.forgotpassword.repository.ForgotPasswordRepository
import com.google.android.gms.tasks.Task
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

class FirebaseForgotPasswordRepositoryImplTest {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var forgotPasswordRepository: ForgotPasswordRepository

    @BeforeEach
    fun setUp() {
        firebaseAuth = mockk()
        forgotPasswordRepository = FirebaseForgotPasswordRepositoryImpl(firebaseAuth)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `forgotPassword should return success when authentication is successful`() = runTest {
        val email = "test@exemple.com"
        val authResult = mockk<Void>()
        val task = mockk<Task<Void>>()

        every { task.isSuccessful } returns true
        every { task.result } returns authResult
        every { firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(any()) } answers {
            firstArg<com.google.android.gms.tasks.OnCompleteListener<Void>>().onComplete(task)
            task
        }

        val result = forgotPasswordRepository.forgotPassword(email)

        assertTrue(result.isSuccess)
        assertEquals(true, result.getOrNull())
    }

    // failure
    @Test
    fun `forgotPassword should return failure when authentication fails`() = runTest {
        val email = "test@exemple.com"
        val task = mockk<Task<Void>>()
        val exception = Exception("Authentication failed")

        every { task.isSuccessful } returns false
        every { task.exception } returns exception
        every { firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(any()) } answers {
            firstArg<com.google.android.gms.tasks.OnCompleteListener<Void>>().onComplete(task)
            task
        }

        val result = forgotPasswordRepository.forgotPassword(email)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

}
