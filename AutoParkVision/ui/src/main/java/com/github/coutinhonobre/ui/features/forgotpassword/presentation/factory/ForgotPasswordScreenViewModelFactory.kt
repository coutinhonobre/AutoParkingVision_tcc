package com.github.coutinhonobre.ui.features.forgotpassword.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.coutinhonobre.domain.feature.forgotpassword.usecase.ForgotPasswordUseCase
import com.github.coutinhonobre.ui.features.forgotpassword.presentation.viewmodel.ForgotPasswordScreenViewModel

class ForgotPasswordScreenViewModelFactory(
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForgotPasswordScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ForgotPasswordScreenViewModel(forgotPasswordUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}