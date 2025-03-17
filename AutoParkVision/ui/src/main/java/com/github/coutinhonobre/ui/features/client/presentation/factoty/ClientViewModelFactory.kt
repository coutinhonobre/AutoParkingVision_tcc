package com.github.coutinhonobre.ui.features.client.presentation.factoty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.coutinhonobre.domain.feature.client.usecase.GetClientUseCase
import com.github.coutinhonobre.domain.feature.client.usecase.SaveClientUseCase
import com.github.coutinhonobre.ui.features.client.presentation.viewmodel.ClientViewModel

class ClientViewModelFactory(
    private val clientId: String,
    private val saveClientUseCase: SaveClientUseCase,
    private val getClientUseCase: GetClientUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClientViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ClientViewModel(
                clientId = clientId,
                saveClientUseCase = saveClientUseCase,
                getClientUseCase = getClientUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}