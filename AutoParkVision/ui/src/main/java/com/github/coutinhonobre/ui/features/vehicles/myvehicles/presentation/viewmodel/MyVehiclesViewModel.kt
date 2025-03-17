package com.github.coutinhonobre.ui.features.vehicles.myvehicles.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.github.coutinhonobre.domain.feature.client.usecase.AddVehicleToClientUseCase
import com.github.coutinhonobre.domain.feature.client.usecase.GetClientUseCase
import com.github.coutinhonobre.domain.feature.vehicle.usecase.GetFilterVehiclesUseCase
import com.github.coutinhonobre.ui.base.viewmodel.BaseViewModel
import com.github.coutinhonobre.ui.base.viewmodel.ViewAction
import com.github.coutinhonobre.ui.base.viewmodel.ViewState
import com.github.coutinhonobre.ui.features.vehicles.model.VehicleItemList
import kotlinx.coroutines.launch

class MyVehiclesViewModel(
    private val getFilterVehiclesUseCase: GetFilterVehiclesUseCase,
    private val getClientUseCase: GetClientUseCase,
    private val addVehicleToClientUseCase: AddVehicleToClientUseCase,
    private val clientId: String = "",
    private val nameClient: String = ""
) : BaseViewModel<MyVehiclesViewAction, MyVehiclesViewState>() {

    override fun initialState(): MyVehiclesViewState = MyVehiclesViewState.Loading
    override fun handleEvent(action: MyVehiclesViewAction) {
        when (action) {
            is MyVehiclesViewAction.Retry -> {
                searchClient()
            }
            is MyVehiclesViewAction.AddVehicle -> {
                addVehicle(action.plate)
            }
        }
    }

    init {
        searchClient()
    }

    private fun searchClient() {
        viewModelScope.launch {
            getClientUseCase(clientId).onSuccess {
                getAllVehicles(
                    plates = it.vehicles
                )
            }.onFailure {
                postState(MyVehiclesViewState.Error)
            }
        }
    }

    private fun getAllVehicles(
        plates: List<String>
    ) {
        viewModelScope.launch {
            postState(MyVehiclesViewState.Loading)
            getFilterVehiclesUseCase(
                plates = plates
            ).onSuccess { vehicles ->
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
                    postState(MyVehiclesViewState.Empty)
                } else {
                    postState(MyVehiclesViewState.Idle(list, nameClient))
                }

            }.onFailure {
                postState(MyVehiclesViewState.Error)
            }

        }
    }

    private fun addVehicle(plate: String) {
        viewModelScope.launch {
            addVehicleToClientUseCase(
                clientId = clientId,
                vehicleId = plate
            ).onSuccess {
                searchClient()
            }.onFailure {
                postState(MyVehiclesViewState.ErrorAddVehicle)
            }
        }
    }

}

sealed class MyVehiclesViewAction: ViewAction {
    data object Retry: MyVehiclesViewAction()
    data class AddVehicle(val plate: String): MyVehiclesViewAction()
}

sealed class MyVehiclesViewState: ViewState {
    data object Loading: MyVehiclesViewState()
    data object Empty: MyVehiclesViewState()
    data class Idle(
        val list: List<VehicleItemList>,
        val nameClient: String
    ): MyVehiclesViewState()
    data object Error: MyVehiclesViewState()
    data object ErrorAddVehicle: MyVehiclesViewState()
}