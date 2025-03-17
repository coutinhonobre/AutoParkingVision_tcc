package com.github.coutinhonobre.licenseplate.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.coutinhonobre.domain.feature.parkingactivitylog.usecase.SaveParkingActivityLogUseCase
import com.github.coutinhonobre.licenseplate.presentation.viewmodel.LicensePlateViewModel

class LicensePlateViewModelFactory(
    private val saveParkingActivityLogUseCase: SaveParkingActivityLogUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LicensePlateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LicensePlateViewModel(
                saveParkingActivityLogUseCase = saveParkingActivityLogUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}