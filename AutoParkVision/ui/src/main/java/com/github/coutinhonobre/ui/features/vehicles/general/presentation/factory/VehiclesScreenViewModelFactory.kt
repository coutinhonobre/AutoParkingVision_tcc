package com.github.coutinhonobre.ui.features.vehicles.general.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.coutinhonobre.domain.feature.vehicle.usecase.GetAllVehiclesUseCase
import com.github.coutinhonobre.ui.features.vehicles.general.presentation.viewmodel.VehiclesScreenViewModel

class VehiclesScreenViewModelFactory(
    private val getAllVehiclesUseCase: GetAllVehiclesUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VehiclesScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VehiclesScreenViewModel(
                getAllVehiclesUseCase = getAllVehiclesUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
