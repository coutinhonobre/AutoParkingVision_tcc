package com.github.coutinhonobre.ui.features.clientslist.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.github.coutinhonobre.domain.feature.clientslist.exception.ClientListNoClientsFoundException
import com.github.coutinhonobre.domain.feature.clientslist.exception.ClientListUnknownErrorException
import com.github.coutinhonobre.domain.feature.clientslist.usecase.ClientListUseCase
import com.github.coutinhonobre.ui.base.viewmodel.BaseViewModel
import com.github.coutinhonobre.ui.base.viewmodel.ViewAction
import com.github.coutinhonobre.ui.base.viewmodel.ViewState
import com.github.coutinhonobre.ui.features.clientslist.presentation.model.ClientItemList
import kotlinx.coroutines.launch

class ClientsScreenViewModel(
    private val getClientsUseCase: ClientListUseCase
) : BaseViewModel<ClientsScreenViewAction, ClientsScreenViewState>() {

    override fun initialState(): ClientsScreenViewState = ClientsScreenViewState.Loading

    init {
        getClients()
    }

    override fun handleEvent(action: ClientsScreenViewAction) {
        when (action) {
            is ClientsScreenViewAction.Retry -> {
                getClients()
            }
        }
    }

    private fun getClients() {
        viewModelScope.launch {
            postState(ClientsScreenViewState.Loading)
            getClientsUseCase().onSuccess { client ->
                val list = client.map {
                    ClientItemList(
                        id = it.id,
                        name = it.name,
                        email = it.email,
                        cellphone = it.cellphone
                    )
                }

                if (list.isEmpty()) {
                    postState(ClientsScreenViewState.Empty)
                } else {
                    postState(ClientsScreenViewState.Idle(list))
                }

            }.onFailure {
                when (it) {
                    is ClientListUnknownErrorException -> postState(ClientsScreenViewState.Empty)
                    is ClientListNoClientsFoundException -> postState(ClientsScreenViewState.Empty)
                    else -> postState(ClientsScreenViewState.Error)
                }
            }
        }
    }

}

sealed class ClientsScreenViewAction: ViewAction {
    data object Retry: ClientsScreenViewAction()
}

sealed class ClientsScreenViewState : ViewState {
    data object Loading: ClientsScreenViewState()
    data object Empty: ClientsScreenViewState()
    data class Idle(val clients: List<ClientItemList>): ClientsScreenViewState()
    data object Error: ClientsScreenViewState()
}