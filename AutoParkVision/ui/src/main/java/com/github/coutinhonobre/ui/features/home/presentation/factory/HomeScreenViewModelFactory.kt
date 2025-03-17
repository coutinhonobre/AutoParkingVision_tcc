package com.github.coutinhonobre.ui.features.home.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.coutinhonobre.domain.feature.parking.repository.ParkingRepository
import com.github.coutinhonobre.domain.feature.parking.usecase.GetParkingUseCase
import com.github.coutinhonobre.domain.feature.parkingactivitylog.repository.ParkingActivityLogRepository
import com.github.coutinhonobre.domain.feature.parkingactivitylog.usecase.GetTodayEntryExitCountUseCase
import com.github.coutinhonobre.domain.feature.parkingactivitylog.usecase.ListParkingActivityLogUseCase
import com.github.coutinhonobre.ui.features.home.presentation.viewmodel.HomeScreenViewModel

class HomeScreenViewModelFactory(
    private val listParkingActivityLogUseCase: ListParkingActivityLogUseCase,
    private val parkingRepository: ParkingRepository,
    private val getTodayEntryExitCountUseCase: GetTodayEntryExitCountUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeScreenViewModel(
                listParkingActivityLogUseCase = listParkingActivityLogUseCase,
                getParkingUseCase = GetParkingUseCase(
                    parkingRepository = parkingRepository
                ),
                getTodayEntryExitCountUseCase = getTodayEntryExitCountUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}