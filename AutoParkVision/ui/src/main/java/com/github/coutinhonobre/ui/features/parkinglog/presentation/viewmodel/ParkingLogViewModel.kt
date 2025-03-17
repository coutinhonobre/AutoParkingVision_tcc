package com.github.coutinhonobre.ui.features.parkinglog.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.github.coutinhonobre.domain.feature.parkingactivitylog.model.ParkingLogEventType
import com.github.coutinhonobre.domain.feature.parkingactivitylog.usecase.FilterParkingLogUseCase
import com.github.coutinhonobre.domain.feature.parkingactivitylog.usecase.ListParkingActivityLogUseCase
import com.github.coutinhonobre.ui.base.viewmodel.BaseViewModel
import com.github.coutinhonobre.ui.base.viewmodel.ViewAction
import com.github.coutinhonobre.ui.base.viewmodel.ViewState
import com.github.coutinhonobre.ui.features.home.presentation.model.ParkingLogModel
import kotlinx.coroutines.launch

class ParkingLogViewModel(
    private val listParkingActivityLogUseCase: ListParkingActivityLogUseCase,
    private val filterParkingLogUseCase: FilterParkingLogUseCase
) : BaseViewModel<ParkingLogViewAction, ParkingLogViewState>() {

    override fun initialState(): ParkingLogViewState = ParkingLogViewState.Loading

    init {
        getParkingLogs()
    }

    override fun handleEvent(action: ParkingLogViewAction) {
        when (action) {
            is ParkingLogViewAction.Retry -> {
                getParkingLogs()
            }
            is ParkingLogViewAction.Filter -> {
                filterParkingLogs(action.licensePlate, action.startDate, action.endDate)
            }
        }
    }

    private fun filterParkingLogs(licensePlate: String, startDate: Long, endDate: Long) {
        viewModelScope.launch {
            postState(ParkingLogViewState.Loading)
            filterParkingLogUseCase(licensePlate, startDate, endDate).onSuccess { parkingLogs ->
                if (parkingLogs.isEmpty()) {
                    postState(ParkingLogViewState.Empty)
                } else {
                    postState(ParkingLogViewState.Idle(parkingLogs.map {
                        ParkingLogModel(
                            eventType = when (it.eventType) {
                                ParkingLogEventType.ENTRY -> "Entrada"
                                ParkingLogEventType.EXIT -> "Saída"
                            },
                            licensePlate = it.licensePlate,
                            timestamp = it.timestamp
                        )
                    }))
                }
            }.onFailure {
                postState(ParkingLogViewState.Error(it.message ?: "Erro ao buscar atividades de estacionamento"))
            }
        }
    }

    private fun getParkingLogs() {
        viewModelScope.launch {
            postState(ParkingLogViewState.Loading)
            listParkingActivityLogUseCase().onSuccess { parkingLogs ->
                if (parkingLogs.isEmpty()) {
                    postState(ParkingLogViewState.Empty)
                } else {
                    postState(ParkingLogViewState.Idle(parkingLogs.map {
                        ParkingLogModel(
                            eventType = when (it.eventType) {
                                ParkingLogEventType.ENTRY -> "Entrada"
                                ParkingLogEventType.EXIT -> "Saída"
                            },
                            licensePlate = it.licensePlate,
                            timestamp = it.timestamp,
                        )
                    }))
                }
            }.onFailure {
                postState(ParkingLogViewState.Error(it.message ?: "Erro ao buscar atividades de estacionamento"))
            }
        }
    }

}

sealed class ParkingLogViewAction : ViewAction {
    data object Retry : ParkingLogViewAction()

    data class Filter(
        val licensePlate: String,
        val startDate: Long,
        val endDate: Long
    ) : ParkingLogViewAction()
}

sealed class ParkingLogViewState : ViewState {
    data object Loading : ParkingLogViewState()
    data object Empty : ParkingLogViewState()
    data class Error(
        val message: String = "Erro ao buscar atividades de estacionamento"
    ) : ParkingLogViewState()
    data class Idle(val parkingLogs: List<ParkingLogModel>) : ParkingLogViewState()
}