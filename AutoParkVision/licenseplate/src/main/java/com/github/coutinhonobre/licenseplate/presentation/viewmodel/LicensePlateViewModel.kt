package com.github.coutinhonobre.licenseplate.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.github.coutinhonobre.domain.feature.parkingactivitylog.usecase.SaveParkingActivityLogUseCase
import com.github.coutinhonobre.licenseplate.base.BaseViewModel
import com.github.coutinhonobre.licenseplate.base.ViewAction
import com.github.coutinhonobre.licenseplate.base.ViewState
import kotlinx.coroutines.launch

class LicensePlateViewModel(
    private val saveParkingActivityLogUseCase: SaveParkingActivityLogUseCase
): BaseViewModel<LicensePlateViewAction, LicensePlateViewState>() {
    override fun initialState(): LicensePlateViewState = LicensePlateViewState.Idle

    override fun handleEvent(action: LicensePlateViewAction) {
        when (action) {
            is LicensePlateViewAction.SendLicensePlate -> {
                postState(LicensePlateViewState.Loading)
                viewModelScope.launch {
                    saveParkingActivityLogUseCase.invoke(
                        licensePlate = action.licensePlate
                    ).onSuccess {
                        postState(LicensePlateViewState.Success(it))
                    }.onFailure {
                        postState(
                            LicensePlateViewState.Error(
                                it.message ?: "Erro desconhecido"
                            )
                        )
                    }
                }
            }
        }
    }
}

sealed class LicensePlateViewAction: ViewAction {
    data class SendLicensePlate(val licensePlate: String): LicensePlateViewAction()
}

sealed class LicensePlateViewState: ViewState {
    data object Loading: LicensePlateViewState()
    data object Idle: LicensePlateViewState()
    data class Error(
        val message: String
    ): LicensePlateViewState()
    data class Success(val message: String): LicensePlateViewState()
}