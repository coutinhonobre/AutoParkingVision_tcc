package com.github.coutinhonobre.ui.features.parking.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.coutinhonobre.domain.feature.parking.usecase.SaveParkingUseCase
import com.github.coutinhonobre.domain.feature.parking.usecase.GetParkingUseCase
import com.github.coutinhonobre.domain.feature.parking.usecase.UpdateParkingUseCase
import com.github.coutinhonobre.ui.features.parking.presentation.model.ParkingScreenModel
import com.github.coutinhonobre.ui.features.parking.presentation.viewmodel.ParkingScreenViewModel

class ParkingScreenViewModelFactory(
    private val saveParkingUseCase: SaveParkingUseCase,
    private val getParkingUseCase: GetParkingUseCase,
    private val updateParkingUseCase: UpdateParkingUseCase,
    private val parking: ParkingScreenModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ParkingScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ParkingScreenViewModel(saveParkingUseCase, getParkingUseCase, updateParkingUseCase, parking) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}