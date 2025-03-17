package com.github.coutinhonobre.ui.features.login.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.coutinhonobre.ui.features.splash.presentation.viewmodel.SplashViewModel

class SplashViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SplashViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}