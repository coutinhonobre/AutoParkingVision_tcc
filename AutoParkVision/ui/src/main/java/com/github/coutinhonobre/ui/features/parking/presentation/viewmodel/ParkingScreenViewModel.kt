package com.github.coutinhonobre.ui.features.parking.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.github.coutinhonobre.domain.feature.parking.usecase.SaveParkingUseCase
import com.github.coutinhonobre.domain.feature.parking.model.Parking
import com.github.coutinhonobre.domain.feature.parking.usecase.GetParkingUseCase
import com.github.coutinhonobre.domain.feature.parking.usecase.UpdateParkingUseCase
import com.github.coutinhonobre.ui.base.viewmodel.BaseViewModel
import com.github.coutinhonobre.ui.base.viewmodel.ViewAction
import com.github.coutinhonobre.ui.base.viewmodel.ViewState
import com.github.coutinhonobre.ui.features.parking.presentation.model.ParkingScreenModel
import kotlinx.coroutines.launch

class ParkingScreenViewModel(
    private val saveParkingUseCase: SaveParkingUseCase,
    private val getParkingUseCase: GetParkingUseCase,
    private val updateParkingUseCase: UpdateParkingUseCase,
    private val parking: ParkingScreenModel
) : BaseViewModel<ParkingScreenViewAction, ParkingScreenViewState>() {
    override fun initialState(): ParkingScreenViewState = ParkingScreenViewState.Loading

    init {
        viewModelScope.launch {
            postState(ParkingScreenViewState.Loading)
            getParkingUseCase().onSuccess {
                postState(
                    ParkingScreenViewState.Idle(
                        ParkingScreenModel(
                            id = it.id,
                            name = it.name,
                            capacity = it.capacity
                        )
                    )
                )
            }.onFailure {
                postState(ParkingScreenViewState.Error)
            }
        }
    }

    override fun handleEvent(action: ParkingScreenViewAction) {
        when (action) {
            is ParkingScreenViewAction.SaveParking -> {
                viewModelScope.launch {
                    postState(ParkingScreenViewState.Loading)
                    val parking = Parking(
                        id = action.parking.id,
                        capacity = action.parking.capacity,
                        name = action.parking.name
                    )
                    if (action.parking.id.isNotEmpty()) {
                        updateParking(parking)
                    } else {
                        saveParking(parking)
                    }
                }
            }

            is ParkingScreenViewAction.Retry -> {
                postState(ParkingScreenViewState.Idle(parking))
            }
        }
    }

    private suspend fun ParkingScreenViewModel.saveParking(
        parking: Parking
    ) {
        saveParkingUseCase(parking).onSuccess {
            postState(ParkingScreenViewState.Success)
        }.onFailure {
            postState(ParkingScreenViewState.Error)
        }
    }

    private suspend fun ParkingScreenViewModel.updateParking(
        parking: Parking
    ) {
        updateParkingUseCase(parking).onSuccess {
            postState(ParkingScreenViewState.Success)
        }.onFailure {
            postState(ParkingScreenViewState.Error)
        }
    }

}

sealed class ParkingScreenViewState : ViewState {
    data class Idle(val parking: ParkingScreenModel) : ParkingScreenViewState()
    data object Loading : ParkingScreenViewState()
    data object Error : ParkingScreenViewState()
    data object Success : ParkingScreenViewState()
}

sealed class ParkingScreenViewAction : ViewAction {
    data class SaveParking(val parking: ParkingScreenModel) : ParkingScreenViewAction()
    data object Retry : ParkingScreenViewAction()
}