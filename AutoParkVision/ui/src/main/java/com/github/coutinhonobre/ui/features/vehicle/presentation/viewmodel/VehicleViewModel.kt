package com.github.coutinhonobre.ui.features.vehicle.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.github.coutinhonobre.domain.feature.vehicle.model.AuthorizedVehicle
import com.github.coutinhonobre.domain.feature.vehicle.model.Vehicle
import com.github.coutinhonobre.domain.feature.vehicle.usecase.GetVehicleUseCase
import com.github.coutinhonobre.domain.feature.vehicle.usecase.SaveVehicleUseCase
import com.github.coutinhonobre.ui.base.viewmodel.BaseViewModel
import com.github.coutinhonobre.ui.base.viewmodel.ViewAction
import com.github.coutinhonobre.ui.base.viewmodel.ViewState
import com.github.coutinhonobre.ui.features.vehicle.model.AuthorizedVehicleScreenModel
import com.github.coutinhonobre.ui.features.vehicle.model.VehicleScreeModel
import kotlinx.coroutines.launch

class VehicleViewModel(
    private val vehicleId: String = "",
    private val saveVehicleUseCase: SaveVehicleUseCase,
    private val getVehicleUseCase: GetVehicleUseCase,
) : BaseViewModel<VehicleViewAction, VehicleViewState>() {

    init {
        vehicleId.takeIf { it.isNotBlank() }?.let {
            viewModelScope.launch {
                getVehicleUseCase(it).onSuccess {
                    val vehicleScreenModel = VehicleScreeModel(
                        id = it.id,
                        model = it.model,
                        plate = it.plate,
                        color = it.color,
                        year = it.year,
                        brand = it.brand,
                        authorized = when (it.authorized) {
                            AuthorizedVehicle.AUTHORIZED -> AuthorizedVehicleScreenModel.AUTHORIZED
                            AuthorizedVehicle.UNAUTHORIZED -> AuthorizedVehicleScreenModel.UNAUTHORIZED
                            AuthorizedVehicle.PENDING -> AuthorizedVehicleScreenModel.PENDING
                            AuthorizedVehicle.DENIED -> AuthorizedVehicleScreenModel.DENIED
                        }
                    )
                    postState(VehicleViewState.Idle(vehicleScreenModel))
                }.onFailure {
                    postState(VehicleViewState.Error(it.message ?: "Erro ao buscar veículo"))
                }
            }
        } ?: run {
            postState(VehicleViewState.Idle(VehicleScreeModel()))
        }
    }

    override fun initialState(): VehicleViewState = VehicleViewState.Loading

    override fun handleEvent(action: VehicleViewAction) {
        when (action) {
            is VehicleViewAction.SaveVehicle -> {
                viewModelScope.launch {
                    val vehicle = Vehicle(
                        id = action.vehicle.id,
                        model = action.vehicle.model,
                        plate = action.vehicle.plate,
                        color = action.vehicle.color,
                        year = action.vehicle.year,
                        brand = action.vehicle.brand,
                        authorized = when (action.vehicle.authorized) {
                            AuthorizedVehicleScreenModel.AUTHORIZED -> AuthorizedVehicle.AUTHORIZED
                            AuthorizedVehicleScreenModel.UNAUTHORIZED -> AuthorizedVehicle.UNAUTHORIZED
                            AuthorizedVehicleScreenModel.PENDING -> AuthorizedVehicle.PENDING
                            AuthorizedVehicleScreenModel.DENIED -> AuthorizedVehicle.DENIED
                        }
                    )
                    saveVehicleUseCase(
                        vehicle = vehicle
                    ).onSuccess {
                        postState(VehicleViewState.Success)
                    }.onFailure {
                        postState(VehicleViewState.Error(it.message ?: "Erro ao salvar veículo"))
                    }
                }
            }
        }
    }

}

sealed class VehicleViewAction: ViewAction {
    data class SaveVehicle(
        val vehicle: VehicleScreeModel
    ) : VehicleViewAction()
}

sealed class VehicleViewState: ViewState {
    data object Loading : VehicleViewState()
    data object Success : VehicleViewState()
    data class Error(
        val message: String
    ) : VehicleViewState()
    data class Idle(val vehicle: VehicleScreeModel) : VehicleViewState()
}