package com.github.coutinhonobre.ui.features.vehicle.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.coutinhonobre.domain.feature.vehicle.usecase.GetVehicleUseCase
import com.github.coutinhonobre.domain.feature.vehicle.usecase.SaveVehicleUseCase
import com.github.coutinhonobre.ui.features.vehicle.presentation.viewmodel.VehicleViewModel

class VehicleViewModelFactory(
    private val vehicleId: String,
    private val saveVehicleUseCase: SaveVehicleUseCase,
    private val getVehicleUseCase: GetVehicleUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VehicleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VehicleViewModel(
                vehicleId = vehicleId,
                saveVehicleUseCase = saveVehicleUseCase,
                getVehicleUseCase = getVehicleUseCase,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}