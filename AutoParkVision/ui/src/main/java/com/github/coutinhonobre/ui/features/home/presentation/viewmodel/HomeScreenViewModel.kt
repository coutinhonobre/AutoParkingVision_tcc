package com.github.coutinhonobre.ui.features.home.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.github.coutinhonobre.domain.feature.parking.usecase.GetParkingUseCase
import com.github.coutinhonobre.domain.feature.parkingactivitylog.model.ParkingLogEventType
import com.github.coutinhonobre.domain.feature.parkingactivitylog.usecase.GetTodayEntryExitCountUseCase
import com.github.coutinhonobre.domain.feature.parkingactivitylog.usecase.ListParkingActivityLogUseCase
import com.github.coutinhonobre.ui.base.viewmodel.BaseViewModel
import com.github.coutinhonobre.ui.base.viewmodel.ViewAction
import com.github.coutinhonobre.ui.base.viewmodel.ViewState
import com.github.coutinhonobre.ui.features.home.presentation.model.ParkingLogModel
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val listParkingActivityLogUseCase: ListParkingActivityLogUseCase,
    private val getParkingUseCase: GetParkingUseCase,
    private val getTodayEntryExitCountUseCase: GetTodayEntryExitCountUseCase
) : BaseViewModel<HomeScreenViewAction, HomeScreenViewState>() {
    override fun initialState(): HomeScreenViewState = HomeScreenViewState.Loading

    init {
        loadSavedParkingSpot()
    }

    private fun loadSavedParkingSpot() {
        viewModelScope.launch {
            getTodayEntryExitCountUseCase.invoke().onSuccess { (entryCount, exitCount) ->
                getParkingUseCase.invoke().onSuccess { parking ->
                    listParkingActivityLogUseCase.invoke().onSuccess { parkingLogs ->
                        val emailUser: String = if (parkingLogs.size > 0) parkingLogs[0].emailUser else ""
                        postState(
                            HomeScreenViewState.Idle(
                                capacity = parking.capacity,
                                userEmail = emailUser,
                                parkingLogs = parkingLogs.map {
                                    ParkingLogModel(
                                        eventType = when (it.eventType) {
                                            ParkingLogEventType.ENTRY -> "Entrada"
                                            ParkingLogEventType.EXIT -> "Saída"
                                        },
                                        licensePlate = it.licensePlate,
                                        timestamp = it.timestamp
                                    )
                                }
                            )
                        )
                    }
                }.onFailure {
                    postState(HomeScreenViewState.Failure)
                }
            }
        }
    }

    override fun handleEvent(action: HomeScreenViewAction) {
        when (action) {
            is HomeScreenViewAction.LoadDataRetry -> {
                postState(HomeScreenViewState.Loading)

            }

            is HomeScreenViewAction.ChangeParkingLotEnvironment -> {

            }
        }
    }

}

sealed class HomeScreenViewAction : ViewAction {
    data object LoadDataRetry : HomeScreenViewAction()
    data object ChangeParkingLotEnvironment : HomeScreenViewAction()
}

sealed class HomeScreenViewState : ViewState {
    data class Idle(
        val userEmail: String,
        val parkingLogs: List<ParkingLogModel>,
        val capacity: Int
    ) : HomeScreenViewState()

    data object Loading : HomeScreenViewState()
    data object Failure : HomeScreenViewState()
}