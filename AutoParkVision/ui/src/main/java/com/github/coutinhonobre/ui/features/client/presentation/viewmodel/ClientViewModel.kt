package com.github.coutinhonobre.ui.features.client.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.github.coutinhonobre.domain.feature.client.model.Client
import com.github.coutinhonobre.domain.feature.client.usecase.GetClientUseCase
import com.github.coutinhonobre.domain.feature.client.usecase.SaveClientUseCase
import com.github.coutinhonobre.ui.base.viewmodel.BaseViewModel
import com.github.coutinhonobre.ui.base.viewmodel.ViewAction
import com.github.coutinhonobre.ui.base.viewmodel.ViewState
import com.github.coutinhonobre.ui.features.client.presentation.model.ClientScreenModel
import kotlinx.coroutines.launch

class ClientViewModel(
    private val clientId: String,
    private val saveClientUseCase: SaveClientUseCase,
    private val getClientUseCase: GetClientUseCase
) : BaseViewModel<ClientViewAction, ClientViewState>() {
    override fun initialState(): ClientViewState = ClientViewState.Loading

    init {
        clientId.isNotEmpty().takeIf { it }?.let {
            viewModelScope.launch {
                postState(ClientViewState.Loading)
                getClientUseCase(clientId)
                    .onSuccess { client ->
                        postState(
                            ClientViewState.Idle(
                                ClientScreenModel(
                                    id = client.id,
                                    name = client.name,
                                    email = client.email,
                                    cellphone = client.phone
                                )
                            )
                        )
                    }
                    .onFailure { postState(ClientViewState.Error) }
            }
        } ?: postState(ClientViewState.Idle(ClientScreenModel()))
    }

    override fun handleEvent(action: ClientViewAction) {
        when (action) {
            is ClientViewAction.SaveClient -> {
                viewModelScope.launch {
                    postState(ClientViewState.Loading)
                    val client = Client(
                        id = action.client.id ?: "",
                        name = action.client.name,
                        email = action.client.email,
                        phone = action.client.cellphone
                    )
                    saveClientUseCase(
                        client = client
                    ).onSuccess {
                        postState(ClientViewState.Success)
                    }.onFailure {
                        postState(ClientViewState.Error)
                    }
                }
            }
        }
    }

}

sealed class ClientViewState : ViewState {
    data class Idle(val client: ClientScreenModel) : ClientViewState()
    data object Loading : ClientViewState()
    data object Success : ClientViewState()
    data object Error : ClientViewState()
}

sealed class ClientViewAction : ViewAction {
    data class SaveClient(val client: ClientScreenModel) : ClientViewAction()
}
