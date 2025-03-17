package com.github.coutinhonobre.ui.features.vehicles.general.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.github.coutinhonobre.domain.feature.vehicle.usecase.GetAllVehiclesUseCase
import com.github.coutinhonobre.ui.base.viewmodel.BaseViewModel
import com.github.coutinhonobre.ui.base.viewmodel.ViewAction
import com.github.coutinhonobre.ui.base.viewmodel.ViewState
import com.github.coutinhonobre.ui.features.vehicles.model.VehicleItemList
import kotlinx.coroutines.launch

class VehiclesScreenViewModel(
    private val getAllVehiclesUseCase: GetAllVehiclesUseCase
) : BaseViewModel<VehiclesScreenViewAction, VehiclesScreenViewState>() {

    override fun initialState(): VehiclesScreenViewState = VehiclesScreenViewState.Loading
    override fun handleEvent(action: VehiclesScreenViewAction) {
        when (action) {
            is VehiclesScreenViewAction.Retry -> {
                getAllVehicles()
            }
        }
    }

    init {
        getAllVehicles()
    }

    private fun getAllVehicles() {
        viewModelScope.launch {
            postState(VehiclesScreenViewState.Loading)
            getAllVehiclesUseCase().onSuccess { vehicles ->
                val list = vehicles.map {
                    VehicleItemList(
                        id = it.id,
                        model = it.model,
                        plate = it.plate,
                        color = it.color,
                        year = it.year,
                        brand = it.brand
                    )
                }

                if (list.isEmpty()) {
                    postState(VehiclesScreenViewState.Empty)
                } else {
                    postState(VehiclesScreenViewState.Idle(list))
                }

            }.onFailure {
                postState(VehiclesScreenViewState.Error)
            }

        }
    }

}

sealed class VehiclesScreenViewAction: ViewAction {
    data object Retry: VehiclesScreenViewAction()
}

sealed class VehiclesScreenViewState: ViewState {
    data object Loading: VehiclesScreenViewState()
    data object Empty: VehiclesScreenViewState()
    data class Idle(val list: List<VehicleItemList>): VehiclesScreenViewState()
    data object Error: VehiclesScreenViewState()
}