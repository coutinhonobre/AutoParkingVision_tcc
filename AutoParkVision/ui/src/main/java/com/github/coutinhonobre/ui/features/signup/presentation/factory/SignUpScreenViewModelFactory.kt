package com.github.coutinhonobre.ui.features.signup.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.coutinhonobre.domain.feature.signup.usecase.SignUpUseCase
import com.github.coutinhonobre.ui.features.signup.presentation.viewmodel.SignUpScreenViewModel

class SignUpScreenViewModelFactory(
    private val signUpUseCase: SignUpUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignUpScreenViewModel(signUpUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}