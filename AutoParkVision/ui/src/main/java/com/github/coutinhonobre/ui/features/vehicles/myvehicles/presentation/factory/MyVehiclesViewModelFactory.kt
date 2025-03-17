package com.github.coutinhonobre.ui.features.vehicles.myvehicles.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.coutinhonobre.domain.feature.client.usecase.AddVehicleToClientUseCase
import com.github.coutinhonobre.domain.feature.client.usecase.GetClientUseCase
import com.github.coutinhonobre.domain.feature.vehicle.usecase.GetFilterVehiclesUseCase
import com.github.coutinhonobre.ui.features.vehicles.myvehicles.presentation.viewmodel.MyVehiclesViewModel

class MyVehiclesViewModelFactory(
    private val getFilterVehiclesUseCase: GetFilterVehiclesUseCase,
    private val getClientUseCase: GetClientUseCase,
    private val addVehicleToClientUseCase: AddVehicleToClientUseCase,
    private val clientId: String,
    private val nameClient: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyVehiclesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyVehiclesViewModel(
                getFilterVehiclesUseCase = getFilterVehiclesUseCase,
                getClientUseCase = getClientUseCase,
                addVehicleToClientUseCase = addVehicleToClientUseCase,
                clientId = clientId,
                nameClient = nameClient
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}