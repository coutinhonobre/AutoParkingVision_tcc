package com.github.coutinhonobre.ui.features.gatekeeper.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.coutinhonobre.domain.feature.collaborators.usecase.GetGatekeeperUseCase
import com.github.coutinhonobre.ui.common.AppPreferences
import com.github.coutinhonobre.ui.features.gatekeeper.presentation.viewmodel.GatekeeperViewModel

class GatekeeperViewModelFactory(
    private val appPreferences: AppPreferences,
    private val getGatekeeperUseCase: GetGatekeeperUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GatekeeperViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GatekeeperViewModel(
                appPreferences = appPreferences,
                getGatekeeperUseCase = getGatekeeperUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}