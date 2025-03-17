package com.github.coutinhonobre.ui.features.gatekeeper.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.github.coutinhonobre.domain.feature.collaborators.usecase.GetGatekeeperUseCase
import com.github.coutinhonobre.ui.base.viewmodel.BaseViewModel
import com.github.coutinhonobre.ui.base.viewmodel.ViewAction
import com.github.coutinhonobre.ui.base.viewmodel.ViewState
import com.github.coutinhonobre.ui.common.AppPreferences
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GatekeeperViewModel(
    private val getGatekeeperUseCase: GetGatekeeperUseCase,
    private val appPreferences: AppPreferences
) : BaseViewModel<GatekeeperViewAction, GatekeeperViewState>() {

    private var code = ""

    init {
        loadCachedCode()
    }


    override fun initialState(): GatekeeperViewState = GatekeeperViewState.Loading

    override fun handleEvent(action: GatekeeperViewAction) {
        when (action) {
            GatekeeperViewAction.ValidateRetry -> {
                postState(GatekeeperViewState.Idle(code = code, error = true))
            }
            is GatekeeperViewAction.Validate -> {
                viewModelScope.launch {
                    postState(GatekeeperViewState.Loading)
                    code = action.code
                    getGatekeeperUseCase.validate(action.code)
                        .onSuccess {
                            appPreferences.saveToCache("code", action.code)
                            postState(GatekeeperViewState.Success)
                        }.onFailure {
                            postState(GatekeeperViewState.Error("Codigo inválido"))
                        }
                }
            }
        }
    }

    private fun loadCachedCode() {
        viewModelScope.launch {
            appPreferences.readFromCache("code").collectLatest { code ->
                postState(GatekeeperViewState.Idle(code = code ?: "", error = false))
            }
        }
    }
}

sealed class GatekeeperViewAction : ViewAction {
    data class Validate(val code: String) : GatekeeperViewAction()
    data object ValidateRetry : GatekeeperViewAction()
}

sealed class GatekeeperViewState : ViewState {
    data class Idle(
        val code: String,
        val error: Boolean
    ) : GatekeeperViewState()
    data object Loading : GatekeeperViewState()
    data object Success : GatekeeperViewState()
    data class Error(val message: String) : GatekeeperViewState()
}