package com.github.coutinhonobre.ui.features.parkinglog.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.coutinhonobre.domain.feature.parkingactivitylog.usecase.FilterParkingLogUseCase
import com.github.coutinhonobre.domain.feature.parkingactivitylog.usecase.ListParkingActivityLogUseCase
import com.github.coutinhonobre.ui.features.parkinglog.presentation.viewmodel.ParkingLogViewModel

class ParkingLogViewModelFactory(
    private val listParkingActivityLogUseCase: ListParkingActivityLogUseCase,
    private val filterParkingLogUseCase: FilterParkingLogUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ParkingLogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ParkingLogViewModel(
                listParkingActivityLogUseCase = listParkingActivityLogUseCase,
                filterParkingLogUseCase = filterParkingLogUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}