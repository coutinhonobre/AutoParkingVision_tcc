package com.github.coutinhonobre.ui.features.clientslist.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.coutinhonobre.domain.feature.clientslist.usecase.ClientListUseCase
import com.github.coutinhonobre.ui.features.clientslist.presentation.viewmodel.ClientsScreenViewModel

class ClientsScreenViewModelFactory(
    private val getClientsUseCase: ClientListUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClientsScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ClientsScreenViewModel(getClientsUseCase = getClientsUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}